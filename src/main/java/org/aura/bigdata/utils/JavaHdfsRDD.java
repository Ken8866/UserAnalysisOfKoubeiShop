package org.aura.bigdata.utils;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

public class JavaHdfsRDD {

    public static void main(String[] args) throws Exception{

        SparkConf conf = new SparkConf();
        conf.setAppName("kafka consumer");
        JavaSparkContext jssc = new JavaSparkContext(conf);
        jssc.setLogLevel("WARN");
        JavaRDD<String> rdd = jssc.textFile("hdfs://bigdata:9000/koubei/sqoopdata");
        JavaPairRDD<String, String> shopInfoRDD = rdd.map(s -> new Tuple2<String, String>(s.split(",")[0], s.split(",")[1])).mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, String> stringStringTuple2) throws Exception {
                return stringStringTuple2;
            }
        });
        shopInfoRDD.foreach(x-> System.out.println(x._1()+" ->" + x._2()));
        jssc.stop();
    }
}
