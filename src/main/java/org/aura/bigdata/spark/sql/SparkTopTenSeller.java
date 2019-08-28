package org.aura.bigdata.spark.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
//import org.apache.spark.sql.types._;


import java.util.ArrayList;
import java.util.List;


public class SparkTopTenSeller {
    public static void main(String[] args){
        SparkConf conf = new SparkConf().setAppName("TopTenSeller");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        JavaRDD<String> shopInfoRDD=jsc.textFile("hdfs://bigdata:9000/warehouse/koubei.db/shop_info/shop_info.txt");
        //shopInfoRDD.collect().forEach(System.out::println);
        //create DataFrame
        String schemaString="shop_id,city_name,location_id,per_pay,score,comment_cnt,shop_level,cate_1_name,cate_2_name";
        List<StructField> fields = new ArrayList<>();
        for (String fieldName:schemaString.split(",")){
            StructField field = DataTypes.createStructField(fieldName,DataTypes.StringType, true);
            fields.add(field);
        }
        //System.out.println(fields);
        StructType schema = DataTypes.createStructType(fields);
        System.out.println(schema);
        JavaRDD<Row>rowRDD = shopInfoRDD.map(new Function<String, Row>() {
            @Override
            public Row call(String record) throws Exception {
                String[] attributes = record.split(",");
                return RowFactory.create(attributes);
            }
        });
        //SparkContext sc = new SparkContext();
        SQLContext sqlContext= new SQLContext(jsc);
        Dataset<Row> shopInfoDataFrame=sqlContext.createDataFrame(rowRDD,schema);
        shopInfoDataFrame.show(50);

        JavaRDD<String> userPayRDD=jsc.textFile("hdfs://bigdata:9000/warehouse/koubei.db/user_pay/user_pay.txt");
        userPayRDD.collect().forEach(System.out::println);
        //create DataFrame
        String userPayschemaString="user_id,shop_id,time_stamp";
        List<StructField> userPayfields = new ArrayList<>();
        for (String fieldName:userPayschemaString.split(",")){
            StructField userPayfield = DataTypes.createStructField(fieldName,DataTypes.StringType, true);
            userPayfields.add(userPayfield);
        }
        //System.out.println(fields);
        StructType userPayschema = DataTypes.createStructType(userPayfields);
        System.out.println(userPayschema);
        JavaRDD<Row>userPayrowRDD = userPayRDD.map(new Function<String, Row>() {
            @Override
            public Row call(String record) throws Exception {
                String[] attributes = record.split(",");
                return RowFactory.create(attributes);
            }
        });
        //SparkContext sc = new SparkContext();
        //SQLContext sqlContext= new SQLContext(jsc);
        Dataset<Row> userPayDataFrame=sqlContext.createDataFrame(userPayrowRDD,userPayschema);
        userPayDataFrame.show(50);


    }
}
