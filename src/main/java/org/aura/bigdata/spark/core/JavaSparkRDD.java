package org.aura.bigdata.spark.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.aura.bigdata.modle.UserView;

import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.apache.spark.sql.functions.*;

public class JavaSparkRDD {

    public static void main(String[] args) throws Exception{

        SparkConf conf = new SparkConf();
        conf.setAppName("AnalysisBySparkRDD").setMaster("local[4]");
        JavaSparkContext jssc = new JavaSparkContext(conf);

        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        //1.加载数据
//        //商家特征数据
//        JavaRDD<ShopInfo> shopInfoJavaRDD = spark
////                .read().textFile("hdfs://bigdata:9000/user/bigdata/data/shop_info")
//                .read().textFile("/Users/yongzhiwen/BDP22/dataset/shop_info.txt")
//                .javaRDD()
//                .map(new Function<String, ShopInfo>() {
//                    public ShopInfo call(String str) {
//                        return ShopInfo.parseShopInfo(str);
//                    }
//                });
//        Dataset<Row> shopInfoDS = spark.createDataFrame(shopInfoJavaRDD, ShopInfo.class);
//        //用户支付行为数据
//        JavaRDD<UserPay> userPayJavaRDD = spark
//                .read()
//                .textFile("/Users/yongzhiwen/BDP22/dataset/user_pay.txt")
////                .textFile("hdfs://bigdata:9000/user/bigdata/data/user_pay.txt")
//                .javaRDD()
//                .map(new Function<String, UserPay>() {
//                    public UserPay call(String str) {
//                        return UserPay.parseUserPay(str);
//                    }
//                });
//        Dataset<Row> userPayDS = spark.createDataFrame(userPayJavaRDD, UserPay.class);
        //用户浏览行为数据
        JavaRDD<UserView> userViewJavaRDD = spark
                .read()
                .textFile("/Users/yongzhiwen/BDP22/dataset/user_view.txt")
//                .textFile("hdfs://bigdata:9000/user/bigdata/data/user_pay.txt")
                .javaRDD()
                .map(new Function<String, UserView>() {
                    public UserView call(String str) {
                        return UserView.parseUserView(str);
                    }
                });
        Dataset<Row> userViewDS = spark.createDataFrame(userViewJavaRDD, UserView.class);

        //2.平均交易额最大的前十个商家
        //创建临时视图
//        userPayDS.createOrReplaceTempView("user_pay");
//        Dataset<Row> timeStampRdd = spark.sql("SELECT shopId,date_format(timeStamp, 'yyyy-MM-dd') timeStr,count(userId) payCount FROM user_pay group by shopId,date_format(timeStamp, 'yyyy-MM-dd')");
//
//        timeStampRdd.createOrReplaceTempView("user_pay_new");
//        Dataset<Row> resultRdd = spark.sql("SELECT shopId,sum(payCount)/count(timeStr) avgPayCount FROM user_pay_new group by shopId");
//
//        resultRdd.join(shopInfoDS.select("shopId", "perPay"),"shopId" ).createOrReplaceTempView("user_pay_shop");
//        Dataset<Row> topRdd = spark.sql("SELECT shopId,perPay*avgPayCount avgPerDayPayCount FROM user_pay_shop order by perPay*avgPayCount desc limit 10");
//
//        topRdd.toJSON().foreach((String s) -> System.out.println(s));


        //3.输出北京、上海、广州和深圳四个城市最受欢迎的 5 家奶茶商店和中式快餐编号
        //受欢迎指数=0.7 ✖(平均评分/5) + 0.3 ✖ (平均消费金额/ 最高消费金额)
        //因为给的数据中无法得到"最高消费金额" 故自行修改：受欢迎指数=0.7 ✖(平均评分/5) + 0.3 ✖ (平均消费金额)
//        shopInfoDS.createOrReplaceTempView("shop_info");

//        Dataset<Row> cityAndTeaRDD = spark.sql("select shopId,0.7*score/5+0.3*perPay popularScore,cityName from shop_info where concat(cityName, cateName_3) in ('北京奶茶','上海奶茶','广州奶茶','深圳奶茶')");
//        cityAndTeaRDD.createOrReplaceTempView("shop_score");
//        Dataset<Row> teaTop5Bycity=spark.sql(" select a.shopId,format_number(a.popularScore,2) popularScore,a.cityName,a.rank from (select shopId,popularScore,cityName,row_number() OVER (PARTITION BY cityName ORDER BY popularScore DESC) rank from shop_score) as a where a.rank <= 5 order by a.cityName,a.rank");
//        System.out.println(teaTop5Bycity.toJSON().collectAsList().toString());

//        Dataset<Row> cityAndChineseFoodRDD = spark.sql("select shopId,0.7*score/5+0.3*perPay popularScore,cityName from shop_info where concat(cityName, cateName_3) in ('北京中式快餐','上海中式快餐','广州中式快餐','深圳中式快餐')");
//        cityAndChineseFoodRDD.createOrReplaceTempView("shop_score_food");
//        Dataset<Row> foodTop5Bycity=spark.sql(" select a.shopId,format_number(a.popularScore,2) popularScore,a.cityName,a.rank from (select shopId,popularScore,cityName,row_number() OVER (PARTITION BY cityName ORDER BY popularScore DESC) rank from shop_score_food) as a where a.rank <= 5");
//        System.out.println(foodTop5Bycity.toJSON().collectAsList().toString());

        //4.指定商店输出每天、每月、每周被浏览数量
        userViewDS.createOrReplaceTempView("user_view");
//        String shopId="1197";
//        Dataset<Row> shopViewByDay = spark.sql("SELECT date_format(timeStamp, 'yyyy-MM-dd') timeStr,count(userId) viewCount FROM user_view where shopId='"+shopId+"'group by date_format(timeStamp, 'yyyy-MM-dd') order by date_format(timeStamp, 'yyyy-MM-dd') desc");
//        Dataset<Row> shopViewByMonth = spark.sql("SELECT date_format(timeStamp, 'yyyy-MM') timeStr,count(userId) viewCount FROM user_view where shopId='"+shopId+"'group by date_format(timeStamp, 'yyyy-MM') order by date_format(timeStamp, 'yyyy-MM') desc");
//        Dataset<Row> shopViewByWeek = spark.sql("SELECT date_format(timeStamp, 'yyyy') year,weekofyear(timeStamp) weekOfYear,count(userId) viewCount FROM user_view where shopId='"+shopId+"'group by date_format(timeStamp, 'yyyy') ,weekofyear(timeStamp) order by  date_format(timeStamp, 'yyyy'),weekofyear(timeStamp) desc");
//        System.out.println(shopViewByDay.toJSON().collectAsList().toString());
//        System.out.println(shopViewByMonth.toJSON().collectAsList().toString());
//        System.out.println(shopViewByWeek.toJSON().collectAsList().toString());

        //5.找出浏览次数最多的50个商家，输出所在城市及人均消费
//        Dataset<Row> shopViewTop50 = spark.sql("select * from(SELECT shopId ,count(userId) viewCount FROM user_view group by shopId) order by viewCount desc limit 50 ");
//        shopViewTop50.createOrReplaceTempView("user_view_top50");
//        Dataset<Row> shopViewTop50WithInfo=spark.sql("select t1.shopId,t1.viewCount,t2.cityName,t2.perPay from user_view_top50 t1 left join shop_info t2 on t1.shopId=t2.shopId order by t1.viewCount desc");
//        System.out.println(shopViewTop50WithInfo.toJSON().collectAsList().toString());


        //6.留存分析，对于平均日交易额最 大的前 3 个商家，对他们进行漏斗分析，以浏览行为作为分析目标，输出 2016.10.01~2016.10.31 共 31 天的留存率，输出为矩阵数据
        String shopIdNo1="1629";
        //6.1查所有用户最早登陆时
        Dataset<Row> shopUserFirst = spark.sql("SELECT min(date_format(timeStamp, 'yyyy-MM-dd')) firstTime,userId FROM user_view where shopId='"+shopIdNo1+"' and date_format(timeStamp, 'yyyy-MM-dd')>='2016-10-01' and date_format(timeStamp, 'yyyy-MM-dd')<='2016-10-31' group by userId");
        shopUserFirst.createOrReplaceTempView("shopUserFirst");
        //6.2用户当月最早登陆时间和每天最早登陆时间
        Dataset<Row> shopUserFirstByDay = spark.sql("select t.firstTimeByDay,t.userId,t1.firstTime,datediff(t.firstTimeByDay,t1.firstTime) dateDiff from (SELECT min(date_format(timeStamp, 'yyyy-MM-dd')) firstTimeByDay,userId FROM user_view where shopId='"+shopIdNo1+"' and date_format(timeStamp, 'yyyy-MM-dd')>='2016-10-01' and date_format(timeStamp, 'yyyy-MM-dd')<='2016-10-31' group by userId,date_format(timeStamp, 'yyyy-MM-dd')) t left join shopUserFirst t1 on t.userId=t1.userId");
        shopUserFirstByDay.createOrReplaceTempView("userViewDateDiff");
        //6.3每天登陆距第一天的天数及登陆数
        Dataset<Row> dateDiffCount = spark.sql("select firstTime,dateDiff,count(userId) keepNum from userViewDateDiff group by firstTime,dateDiff");
        Dataset<Row> keepDataResult=dateDiffCount.groupBy("firstTime").pivot("dateDiff").agg(expr("sum(nvl(keepNum,0))")).orderBy("firstTime");
        //keepDataResult  结果格式化,否则keepDataResult.toJSON 里null值的key会被丢弃
        keepDataResult.createOrReplaceTempView("keepDataResult");
//        keepDataResult.printSchema();
        Dataset<Row> keepDataFormatResult=spark.sql("select firstTime,nvl(`0`,0) day_0,nvl(`1`,0) day_1,nvl(`2`,0) day_2,nvl(`3`,0) day_3,nvl(`4`,0) day_4,nvl(`5`,0) day_5 ,nvl(`6`,0) day_6,nvl(`7`,0) day_7,nvl(`8`,0) day_8 ," +
                "nvl(`9`,0) day_9,nvl(`10`,0) day_10,nvl(`11`,0) day_11,nvl(`12`,0) day_12,nvl(`13`,0) day_13,nvl(`14`,0) day_14,nvl(`15`,0) day_15 ,nvl(`16`,0) day_16,nvl(`17`,0) day_17,nvl(`18`,0) day_18,nvl(`19`,0) day_19," +
                "nvl(`20`,0) day_20,nvl(`21`,0) day_21,nvl(`22`,0) day_22,0 day_23,0 day_24,0 day_25 ,nvl(`26`,0) day_26,nvl(`27`,0) day_27,0 day_28,0 day_29,0 day_30,0 day_31" +
                " from keepDataResult ");
        keepDataFormatResult.show();

        System.out.println(keepDataFormatResult.toJSON().collectAsList().toString());
//        keepDataResult.show(31,false);
//        keepDataResult.toJSON().show(5,false);
        jssc.stop();
    }
}
