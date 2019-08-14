package org.aura.bigdata;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
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
import org.aura.bigdata.dao.impl.MysqlDaoHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.function.Consumer;

public class JavaKafkaEventProducer {

    public static void main(String[] args) throws  Exception{

        String filePath = "/home/bigdata/tmp/writedata.txt" ;

        SparkSession sparkSession = SparkSession.builder()
                .appName("write user_pay to kafka")
                .getOrCreate();
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

                        String userId = row.getString(0);
                        String shopId = row.getString(1);
                        String timestamp = row.getString(2) ;

                        //每隔10毫秒将userpay数据写入kafka user_pay主题中，key为userid，value为shopid+“，”+timestamp
                        ProducerRecord<String, String> kvProducerRecord = new ProducerRecord<String, String>(topic, userId,shopId+","+timestamp);
                        kafkaProducer.send(kvProducerRecord);
                        System.out.println("["+new Date(System.currentTimeMillis())+"] kefak msg send: "+"topic: "+topic+" key: "+userId+" value: "+shopId+","+timestamp );
                        try{
                            Thread.sleep(10);
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {

                        }
                    }


                });
            }
        });

        sparkSession.stop();
    }
}
