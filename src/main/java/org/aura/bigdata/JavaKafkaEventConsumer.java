package org.aura.bigdata;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.mortbay.util.ajax.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import scala.Option;
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

        //每隔1秒为一个batch
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
//        jssc.checkpoint("/tmp/kafkaUserPay");
        jssc.sparkContext().setLogLevel("WARN");
        JavaPairInputDStream<String, String> kafkaDstream = KafkaUtils.createDirectStream(jssc,
                String.class, String.class,
                StringDecoder.class, StringDecoder.class,
                kafkaParams,
                new HashSet<String>(Arrays.asList("user_pay")));

        //提取shopid，转换为以shopid为key，value为1的元素构成的Dstream
        JavaPairDStream<String, Long> userPayJavaPairDstream = kafkaDstream.map(new Function<Tuple2<String, String>, Tuple2<String, String>>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, String> record) throws Exception {
                String userId = record._1();
                String shopId_timestamp = record._2();

                String[] data = shopId_timestamp.split(",");
                Tuple2<String,String> tuple2 = null ;
                if(data.length==2){
                    tuple2 = new Tuple2<String,String>(data[0],data[1]) ;
                }else{
                    tuple2 = new Tuple2<>("value# ",shopId_timestamp);
                }
                return tuple2;
            }
        }).mapToPair(new PairFunction<Tuple2<String, String>, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Tuple2<String, String> record) throws Exception {
                return new Tuple2<String, Long>("jiaoyi" + record._1(), Long.valueOf(1));
            }
        });

        //实时统计商家交易次数，每10秒统计一次
        JavaPairDStream<String, Long> countJavaPairDstream = userPayJavaPairDstream.window(Durations.seconds(10)).reduceByKey((x, y) -> x + y);
        JavaMapWithStateDStream<String, Long, Long, Tuple2<String, Long>> shopCountWithStateDStream = countJavaPairDstream.mapWithState(StateSpec.function(new Function3<String, Optional<Long>, State<Long>, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> call(String key, Optional<Long> value, State<Long> state) throws Exception {
                Option<Long> statCount = state.getOption();
                Long sum = value.orElse(Long.valueOf(0));
                if (statCount.isDefined()) {
                    sum += statCount.get();
                }
                state.update(sum);
                return new Tuple2<String, Long>(key, sum);
            }
        }));

        //实时统计商家交易次数的结果保存到redis中
//        countJavaPairDstream.foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
        shopCountWithStateDStream.stateSnapshots().foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
            @Override
            public void call(JavaPairRDD<String, Long> shopIdCountPairRDD) throws Exception {
                shopIdCountPairRDD.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> records) throws Exception {
                        Jedis jedis = JavaRedisClient.getJedisPool().getResource();
                        records.forEachRemaining(new Consumer<Tuple2<String, Long>>() {
                            @Override
                            public void accept(Tuple2<String, Long> record) {
                                jedis.set(record._1(),String.valueOf(record._2()));
                                System.out.println("jiaoyi save success"+record._1()+"->"+record._2());
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
        JavaPairDStream<String, Long> cityPairDStream = userPayJavaPairDstream.window(Durations.seconds(10)).mapToPair(new PairFunction<Tuple2<String, Long>, String, Long>() {
            @Override
            public Tuple2<String, Long> call(Tuple2<String, Long> stringLongTuple2) throws Exception {
                return new Tuple2<>(stringLongTuple2._1().substring(6), Long.valueOf(stringLongTuple2._2()));
            }
        });
        JavaMapWithStateDStream<String, Long, Long, Tuple2<String, Long>> cityCountWithStateDStream = cityPairDStream.mapWithState(StateSpec.function(new Function3<String, Optional<Long>, State<Long>, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> call(String key, Optional<Long> value, State<Long> state) throws Exception {
                Option<Long> statCount = state.getOption();
                Long sum = value.orElse(Long.valueOf(0));
                if (statCount.isDefined()) {
                    sum += statCount.get();
                }
                state.update(sum);
                return new Tuple2<String, Long>(key, sum);
            }
        }));

//        cityPairDStream.foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
        cityCountWithStateDStream.stateSnapshots().foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
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

        jssc.checkpoint("/tmp/kafkaUserPay");

         jssc.start();
         jssc.awaitTermination();

    }
}
