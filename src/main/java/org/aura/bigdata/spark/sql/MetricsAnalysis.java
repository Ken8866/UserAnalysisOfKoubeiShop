package org.aura.bigdata.spark.sql;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.aura.bigdata.dao.EntityDao;
import org.aura.bigdata.dao.base.HbaseDaoBase;
import org.aura.bigdata.dao.impl.EntityDaoImpl;
import org.aura.bigdata.model.*;
import org.aura.bigdata.service.QueryService;
import org.aura.bigdata.service.impl.QueryServiceImpl;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public class MetricsAnalysis implements Serializable {

    /**
     * 需求处理方案设计思路：
     *
     * 1，使用hdfs保存三个月内的数据，可采用3+1的时间周期存放源数据，每满四个月，读取数据的时间点就后移一个月
     * 2，使用定时任务，每天定时读取三个月内的数据进行批处理分析
     * 3，分析结果存入hbase，rowkey为商家id+系统分析的当前时间，rowkey设计要考虑到数据倾斜的问题及查询性能的问题
     * 4，前端业务根据rowkey读取每天分析出来的前一天的分析结果进行展示
     *
     */

    public static final String USER_VIEW_URL = "datasource/user_view.txt" ;
    public static final String USER_PAY_URL = "datasource/user_pay.txt" ;
    public static final String SHOP_INFO_URL = "datasource/shop_info.txt" ;
    public static final String USER_PAY_HDFS_URL = "hdfs://bigdata:9000/koubei/rawdata/user_pay.txt";
    public static final String USER_VIEW_HDFS_URL = "hdfs://bigdata:9000/koubei/rawdata/user_view.txt";
    public static final String SHOP_INFO_HDFS_URL = "hdfs://bigdata:9000/koubei/rawdata/user_view.txt";

    List<Dataset<Row>> userPayAnalysis = null ;
    public static Dataset<Row> shopInfoDataSet = null ;

    public static void main(String[] args) throws Exception{

        SparkSession sparkSession = SparkSession.builder().master("local[*]").appName("userAnalysis").getOrCreate();
        sparkSession.sparkContext().setLogLevel("WARN");
        MetricsAnalysis metricsAnalysis = new MetricsAnalysis();

        List<Dataset<Row>> userPayAnalysis = metricsAnalysis.userPayAnalysis(sparkSession, USER_PAY_URL);
        shopInfoDataSet = metricsAnalysis.shopInfoAnalysis(sparkSession, SHOP_INFO_URL);
        shopInfoDataSet.cache();
        List<Dataset<Row>> userViewAnalysis = metricsAnalysis.userViewAnalysis(sparkSession, USER_VIEW_URL);


        metricsAnalysis.saveAnalysisResult(userPayAnalysis);
        metricsAnalysis.saveAnalysisResult(userViewAnalysis);
        metricsAnalysis.saveShopScore(shopInfoDataSet);

        sparkSession.stop();
    }

    /**
     * 用户支付/商家被支付次数分析
     * @param sparkSession
     * @param dataSource
     * @return
     * @throws Exception
     */
    public List<Dataset<Row>> userPayAnalysis(SparkSession sparkSession,String dataSource) throws Exception{

        ArrayList<Dataset<Row>> datasets = new ArrayList<>();
        JavaRDD<Row> userPayRowRDD = sparkSession.read().text(dataSource).javaRDD();

        JavaRDD<UserPay> userPayRDD = userPayRowRDD.map(new Function<Row, UserPay>() {
            @Override
            public UserPay call(Row v1) throws Exception {
                String[] split = v1.getString(0).split(",");
                UserPay userPay = new UserPay();
                userPay.setUser_id(split[0]);
                userPay.setShop_id(split[1]);
                userPay.setTime_stamp(split[2]);
                return userPay;
            }
        });

        Dataset<Row> userPayDF = sparkSession.createDataFrame(userPayRDD, UserPay.class);

        userPayDF.createOrReplaceTempView("user_pay");
        SQLContext userPaySqlContext = userPayDF.sqlContext();

        String sql1 = "select user_id ,count(distinct shop_id) as last_7_day_payed from user_pay  where trunc(to_date(time_stamp),'MM') BETWEEN date_sub(to_date('2016-10-31'),7) AND to_date('2016-10-31') group by user_id" ;
        String sql2 = "select user_id ,count(distinct shop_id) as last_1_month_payed from user_pay  where trunc(to_date(time_stamp),'MM') BETWEEN add_months(to_date('2016-10-31'),-1) AND to_date('2016-10-31') group by user_id" ;
        String sql3 = "select user_id ,count(distinct shop_id) as last_3_month_payed from user_pay  where trunc(to_date(time_stamp),'MM') BETWEEN add_months(to_date('2016-10-31'),-3) AND to_date('2016-10-31') group by user_id" ;

        String sql4 = "select shop_id ,count(*) as last_7_day_payed from user_pay  where trunc(to_date(time_stamp),'MM') BETWEEN date_sub(to_date('2016-10-31'),7) AND to_date('2016-10-31') group by shop_id" ;
        String sql5 = "select shop_id ,count(*) as last_1_month_payed from user_pay  where trunc(to_date(time_stamp),'MM') BETWEEN add_months(to_date('2016-10-31'),-1) AND to_date('2016-10-31') group by shop_id" ;
        String sql6 = "select shop_id ,count(*) as last_3_month_payed from user_pay where trunc(to_date(time_stamp),'MM') BETWEEN add_months(to_date('2016-10-31'),-3) AND to_date('2016-10-31') group by shop_id" ;

        String[] sqls = new String[]{sql1, sql2, sql3, sql4, sql5, sql6};

        for (int i=0;i< sqls.length;i++){
            Dataset<Row> dataset = userPaySqlContext.sql(sqls[i]);
            datasets.add(dataset);
        }

        return datasets;
    }

    /**
     * 商家数据分析
     * @return
     * @throws Exception
     */
    public Dataset<Row> shopInfoAnalysis(SparkSession sparkSession,String dataSource) throws Exception {
        JavaRDD<Row> userShopInfoRDD = sparkSession.read().text(dataSource).javaRDD();
        JavaRDD<ShopInfo> shopInfoPairRDD = userShopInfoRDD.map(new Function<Row, ShopInfo>() {
            @Override
            public ShopInfo call(Row v1) throws Exception {
                String[] split = v1.getString(0).split(",");
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setShop_id(split[0]);
                shopInfo.setCity_name(split[1]);
                shopInfo.setScore(split[4]);
                return shopInfo;
            }
        });

        Dataset<Row> shopInfoDF = sparkSession.createDataFrame(shopInfoPairRDD,ShopInfo.class);
        shopInfoDF.createOrReplaceTempView("shop_info");
        SQLContext sqlContext = shopInfoDF.sqlContext();

        String sql = "select shop_id, score, city_name from shop_info " ;

        Dataset<Row> shopScoreDS = sqlContext.sql(sql);

        return shopScoreDS ;
    }


    /**
     * 用户浏览/商家被浏览次数分析
     * @param sparkSession
     * @param dataSource
     * @return
     * @throws Exception
     */
    public List<Dataset<Row>> userViewAnalysis(SparkSession sparkSession,String dataSource) throws Exception{

        ArrayList<Dataset<Row>> datasets = new ArrayList<>();
        JavaRDD<Row> userViewRowRDD = sparkSession.read().text(dataSource).javaRDD();

        JavaRDD<UserView> userViewRDD = userViewRowRDD.map(new Function<Row, UserView>() {
            @Override
            public UserView call(Row v1) throws Exception {
                String[] split = v1.getString(0).split(",");
                UserView userView = new UserView();
                userView.setUser_id(split[0]);
                userView.setShop_id(split[1]);
                userView.setTime_stamp(split[2]);
                return userView;
            }
        });

        Dataset<Row> userViewDF = sparkSession.createDataFrame(userViewRDD,UserView.class);
        userViewDF.createOrReplaceTempView("user_view");
        SQLContext userViewSqlContext = userViewDF.sqlContext();

        String sql1 = "select user_id ,count(distinct shop_id) as last_7_day_review from user_view  where trunc(to_date(time_stamp),'MM') BETWEEN date_sub(to_date('2016-10-31'),7) AND to_date('2016-10-31') group by user_id" ;
        String sql2 = "select user_id ,count(distinct shop_id) as last_1_month_review from user_view  where trunc(to_date(time_stamp),'MM') BETWEEN add_months(to_date('2016-10-31'),-1) AND to_date('2016-10-31') group by user_id" ;
        String sql3 = "select user_id ,count(distinct shop_id) as last_3_month_review from user_view  where trunc(to_date(time_stamp),'MM') BETWEEN add_months(to_date('2016-10-31'),-3) AND to_date('2016-10-31') group by user_id" ;

        String sql4 = "select shop_id ,count(*) as last_7_day_reviewed from user_view  where trunc(to_date(time_stamp),'MM') BETWEEN date_sub(to_date('2016-10-31'),7) AND to_date('2016-10-31') group by shop_id" ;
        String sql5 = "select shop_id ,count(*) as last_1_month_reviewed from user_view  where trunc(to_date(time_stamp),'MM') BETWEEN add_months(to_date('2016-10-31'),-1) AND to_date('2016-10-31') group by shop_id" ;
        String sql6 = "select shop_id ,count(*) as last_3_month_reviewed from user_view  where trunc(to_date(time_stamp),'MM') BETWEEN add_months(to_date('2016-10-31'),-3) AND to_date('2016-10-31') group by shop_id" ;


        String sql7 = "select user_id, shop_id, count(*) as count from user_view group by user_id, shop_id order by user_id, shop_id " ;

        String[] sqls = new String[]{sql1, sql2, sql3, sql4, sql5, sql6};

        for (String sql:sqls){
            Dataset<Row> dataset= userViewSqlContext.sql(sql);
            datasets.add(dataset);
        }

        Dataset<Row> userCountView = userViewSqlContext.sql(sql7);
        userCountView.createOrReplaceTempView("user_view_count");
        Dataset<Row> user_shopCountDS = userCountView.sqlContext().sql("select user_id ,shop_id from user_view_count where (count,shop_id) in (select max(count) maxcount,shop_id from user_view_count group by shop_id) ");
        Dataset<Row> user_cityDS = user_shopCountDS.join(shopInfoDataSet, "shop_id");
        Dataset<Row> user_cityDS2 = user_cityDS.select("user_id", "city_name");
        datasets.add(user_cityDS2);

        return datasets;
    }

    public void saveShopScore(Dataset<Row> shopInfoDataSet) throws Exception{
        Dataset<Row> shopScoreDS = shopInfoDataSet.select("shop_id", "score");
        List<Dataset<Row>> shopScoreDSList = new ArrayList<>();
        shopScoreDSList.add(shopScoreDS);
        saveAnalysisResult(shopScoreDSList);
    }


    /**
     * 保存分析结果
     * @param results
     * @throws Exception
     */
    public void saveAnalysisResult(List<Dataset<Row>> results) throws Exception{

        for(Dataset<Row> result:results){
            StructType schema = result.schema();
            StructField[] fields = schema.fields();
            String tableKey = fields[0].name();
            String qulifier = fields[1].name();

            result.foreachPartition(new ForeachPartitionFunction<Row>() {
                @Override
                public void call(Iterator<Row> t) throws Exception {

                    Configuration conf = HBaseConfiguration.create();
                    conf.set("hbase.zookeeper.quorum","hadoopnode");
                    conf.set("zookeeper.znode.parent", "/hbase");
                    Connection conn = ConnectionFactory.createConnection(conf);
                    Table userlabels = conn.getTable(TableName.valueOf("userlabels"));
                    Table shoplabels = conn.getTable(TableName.valueOf("shoplabels"));

                    t.forEachRemaining(new Consumer<Row>() {
                        @Override
                        public void accept(Row row) {
                            try {
                                Entity entity = new Entity();
                                entity.setRow(String.valueOf(row.get(0)));
                                HashMap<String, Map<String,String>>cell = new HashMap<>(1);
                                Map<String,String> qualifierValueMap = new HashMap<String,String>();
                                String value = "" ;
                                if(qulifier.startsWith("last_")){
                                    value = String.valueOf(row.getLong(1));
                                    qualifierValueMap.put(qulifier,String.valueOf(row.getLong(1)));
                                }else{
                                    value = row.getString(1);
                                    qualifierValueMap.put(qulifier,row.getString(1));
                                }
                                cell.put("colFmly",qualifierValueMap);
                                entity.setCell(cell);
                                System.out.println(JSON.toJSONString(entity));

                                Put put = new Put(Bytes.toBytes(row.getString(0)));
                                put.addColumn(Bytes.toBytes("colFmly"),Bytes.toBytes(qulifier),Bytes.toBytes(value));
                                if("shop_id".equals(tableKey)){
                                    shoplabels.put(put);
                                }else{
                                    userlabels.put(put);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    if(userlabels!=null) userlabels.close();
                    if(shoplabels!=null) shoplabels.close();
                    if(conn!=null) conn.close();

                }
            });
        }

    }

    /**
     * 生成保存实体对象
     * @param result
     * @return
     */
    public Entity generateLabelEntity(Dataset<Row> result){

        Entity entity = null ;

        StructType schema = result.schema();
        StructField[] fields = schema.fields();

        if("shop_id".equals(fields[0].name())){
            entity = new Entity<ShopLabels>();
            entity.setName(ShopLabels.class.getSimpleName().toLowerCase());
        }else {
            entity = new Entity<UserLabels>();
            entity.setName(UserLabels.class.getSimpleName().toLowerCase());
        }

        return entity ;
    }

}
