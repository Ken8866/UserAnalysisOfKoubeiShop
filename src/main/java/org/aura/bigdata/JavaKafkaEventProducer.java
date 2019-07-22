package org.aura.bigdata;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.*;
import java.util.function.Consumer;

public class JavaKafkaEventProducer {

    public static void main(String[] args) throws  Exception{

        SparkSession sparkSession = SparkSession.builder().appName("write user_pay to kafka").getOrCreate();
        Dataset<Row> dataset = sparkSession.read().csv("hdfs://bigdata:9000/koubei/rawdata/user_pay.txt");
        dataset.foreachPartition(new ForeachPartitionFunction<Row>() {
            @Override
            public void call(Iterator<Row> t) throws Exception {
                String brokers = "bigdata:9092";
                String topic = "user_pay" ;
                Properties props = new Properties();
                props.put("bootstrap.servers",brokers);
                props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

                KafkaProducer kafkaProducer = new KafkaProducer(props);
                t.forEachRemaining(new Consumer<Row>() {
                    @Override
                    public void accept(Row row) {
                        JSONObject event = new JSONObject();
                        event.put(row.getString(0),row.getString(1)+"."+row.getString(2));
                        ProducerRecord<String, String> kvProducerRecord = new ProducerRecord<String, String>(topic,event.toString());
                        kafkaProducer.send(kvProducerRecord);
                        System.out.println("["+new Date(System.currentTimeMillis())+"] Message send: "+event);
                        try{
                            Thread.sleep(10);
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            ;
                        }
                    }
                });
            }
        });

        sparkSession.stop();
    }
}
