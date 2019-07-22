package org.aura.bigdata;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.mortbay.util.ajax.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import scala.Tuple2;

import java.util.*;
import java.util.function.Consumer;

public class JavaKafkaEventConsumer {

    public static void main(String[] args) throws Exception{

        Map<String,String> kafkaParams = new HashMap<String,String>();
        kafkaParams.put("bootstrap.servers","bigdata:9092");
        kafkaParams.put("serializer.class", "kafka.serializer.StringEncoder");


        SparkConf conf = new SparkConf();
        conf.setAppName("kafka consumer");

        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(10));
        jssc.sparkContext().setLogLevel("WARN");
        JavaPairInputDStream<String, String> kafkaDstream = KafkaUtils.createDirectStream(jssc,
                String.class, String.class,
                StringDecoder.class, StringDecoder.class,
                kafkaParams,
                new HashSet<String>(Arrays.asList("user_pay")));

        JavaPairDStream<String, Long> userPayJavaPairDstream = kafkaDstream.map(new Function<Tuple2<String, String>, Tuple2<String, String>>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, String> v1) throws Exception {
                JSONObject event = JSONObject.parseObject(v1._2());
                Set<Map.Entry<String, Object>> entries = event.entrySet();
                String value = (String)entries.iterator().next().getValue();
                String[] data = value.split("\\.");
                Tuple2<String,String> tuple2 = null ;
                if(data.length==2){
                    tuple2 = new Tuple2<String,String>(data[0],data[1]) ;
                }else{
                    tuple2 = new Tuple2<>("value# ",value);
                }
                return tuple2;
            }
        }).mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> record) throws Exception {
                return new Tuple2<String, Long>("jiaoyi" + record._1(), Long.valueOf(1));
            }
        });

        JavaPairDStream<String, Long> countJavaPairDstream = userPayJavaPairDstream.window(Durations.minutes(1)).reduceByKey((x, y) -> x + y);

        //count and sava transaction by shop_id
        countJavaPairDstream.foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
            @Override
            public void call(JavaPairRDD<String, Long> stringLongJavaPairRDD) throws Exception {
                stringLongJavaPairRDD.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> tuple2Iterator) throws Exception {
                        Jedis jedis = JavaRedisClient.getJedisPool().getResource();
                        tuple2Iterator.forEachRemaining(new Consumer<Tuple2<String, Long>>() {
                            @Override
                            public void accept(Tuple2<String, Long> stringLongTuple2) {
                                jedis.set(stringLongTuple2._1(),String.valueOf(stringLongTuple2._2()));
                                System.out.println("jiaoyi save success"+stringLongTuple2._1()+"->"+stringLongTuple2._2());
                            }
                        });
                        jedis.close();
                    }
                });
            }
        });

        JavaSparkContext sc = jssc.sparkContext();
        sc.setLogLevel("WARN");
        JavaRDD<String> rdd = sc.textFile("hdfs://bigdata:9000/koubei/sqoopdata");
        JavaPairRDD<String, String> shopInfoRDD = rdd.map(s -> new Tuple2<String, String>(s.split(",")[0], s.split(",")[1])).mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, String> stringStringTuple2) throws Exception {
                return stringStringTuple2;
            }
        });

        //count and save transaction by city
        userPayJavaPairDstream.window(Durations.minutes(1)).mapToPair(new PairFunction<Tuple2<String, Long>, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Tuple2<String, Long> stringLongTuple2) throws Exception {
                return new Tuple2<>(stringLongTuple2._1().substring(6),Long.valueOf(stringLongTuple2._2()));
            }
        }).foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
            @Override
            public void call(JavaPairRDD<String, Long> stringLongJavaPairRDD) throws Exception {

                JavaPairRDD<String, Long> cityPairRDD = stringLongJavaPairRDD.join(shopInfoRDD).mapToPair(new PairFunction<Tuple2<String, Tuple2<Long, String>>, String, Long>() {
                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, Tuple2<Long, String>> stringTuple2Tuple2) throws Exception {
                        return new Tuple2<String, Long>(stringTuple2Tuple2._2()._2(), stringTuple2Tuple2._2()._1());
                    }
                });

                cityPairRDD.reduceByKey((x,y) -> x+y ).foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {

                    @Override
                    public void call(Iterator<Tuple2<String, Long>> tuple2Iterator) throws Exception {
                        Jedis jedis = JavaRedisClient.getJedisPool().getResource();
                        tuple2Iterator.forEachRemaining(new Consumer<Tuple2<String, Long>>() {
                            @Override
                            public void accept(Tuple2<String, Long> stringLongTuple2) {
                                jedis.set("交易"+stringLongTuple2._1(),String.valueOf(stringLongTuple2._2()));
                                System.out.println("交易城市存储成功！！！"+stringLongTuple2._1()+"->"+stringLongTuple2._2());
                            }
                        });
                        jedis.close();
                    }
                });
            }
        });

         jssc.start();
         jssc.awaitTermination();

    }
}
