package org.aura.bigdata;

import com.alibaba.fastjson.JSON;
import org.aura.bigdata.dao.base.HbaseDaoBase;
import org.aura.bigdata.model.*;
import org.aura.bigdata.service.QueryService;
import org.aura.bigdata.service.impl.QueryServiceImpl;
import org.aura.bigdata.utils.AppConstants;
import org.aura.bigdata.utils.AppUtils;
import org.aura.bigdata.utils.QueryCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceClient {

    public static final String USER_VIEW_URL = "datasource/user_view.txt" ;
    public static final String USER_PAY_URL = "datasource/user_pay.txt" ;
    public static final String SHOP_INFO_URL = "datasource/shop_info.txt" ;

    static {
        try {
            HbaseDaoBase.initConf();
            AppUtils.initShopInfo(SHOP_INFO_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务调用或测试方法入口
     * @param args 返回查询结果为Json串
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        String resString = findUserLabels();
        System.out.println(resString);
    }


    /**
     * 创建一个userLabels的查询Service
     * @return
     * @throws Exception
     */
    public static QueryService getUserLabelsQueryService() throws Exception {
        QueryService userLabelsQueryService = new QueryServiceImpl<UserLabels>();
        UserLabels userLabels = new UserLabels();;
        userLabelsQueryService.setT(userLabels);
        return  userLabelsQueryService ;
    }

    /**
     * 创建一个shopLabels的查询Service
     * @return
     * @throws Exception
     */
    public static QueryService getShopLabelsQueryService() throws Exception {
        QueryService shopLabelsQueryService = new QueryServiceImpl<ShopLabels>();
        ShopLabels shopLabels = new ShopLabels();;
        shopLabelsQueryService.setT(shopLabels);
        return  shopLabelsQueryService ;
    }

    /**
     * 创建一个userBill的查询Service
     * @return
     * @throws Exception
     */
    public static QueryService getUserPayQueryService() throws Exception {
        QueryService userPayQueryService = new QueryServiceImpl<UserPay>();
        UserPay userPay = new UserPay();;
        userPayQueryService.setT(userPay);
        return  userPayQueryService ;
    }

    /**
     * Count UserBill总数，内部使用scan扫描实现
     * @return
     * @throws Exception
     */
    public static String countUserPay() throws Exception{

        QueryService<UserPay> userPayQueryService = getUserPayQueryService();
        UserPay userPay = userPayQueryService.getT();
        Entity<UserPay> userPayEntity = new Entity<UserPay>();
        userPayEntity.setT(userPay);
//        userPayEntity.setColumnFamily(new String[]{"colFmly"});
        String jsonParam = JSON.toJSONString(userPayEntity);
        String resString = userPayQueryService.execute(jsonParam, AppConstants.SVC_TYPE_COUNT);
//        String resString = userPayQueryService.execute(jsonParam,AppConstants.SVC_TYPE_CREATE_TABLE);

        return resString;
    }

    /**
     * 根据user_id 及 times_tamp查询账单
     * @return
     * @throws Exception
     */
    public static String findUserPay() throws Exception{
        QueryService<UserPay> userPayQueryService = getUserPayQueryService();
        UserPay userPay = userPayQueryService.getT();
        Entity<UserPay> userPayEntity = new Entity<UserPay>();
        userPayEntity.setT(userPay);
        userPayEntity.setColumnFamily(new String[]{"colFmly"});
        userPayEntity.setRow("9990843_1862_2015-11-24");

        String jsonParam = JSON.toJSONString(userPayEntity);
        String resString = userPayQueryService.execute(jsonParam,AppConstants.SVC_TYPE_FIND);
        return resString ;
    }


    /**
     * 根据用户id查询用户标签
     * @return
     * @throws Exception
     */
    public static String findUserLabels() throws Exception{

       /* Current count: 11799000, row: 9659170
        Current count: 11800000, row: 9660874
        Current count: 11801000, row: 9662519
        Current count: 11802000, row: 9664207
        Current count: 11803000, row: 9665899
        Current count: 11804000, row: 9667582
        Current count: 11805000, row: 9669262
        Current count: 11806000, row: 9670959
        Current count: 11807000, row: 9672640
        Current count: 11808000, row: 9674384
        Current count: 11809000, row: 9676099
        Current count: 11810000, row: 9677774*/


       /* {"cell":{"colFmly":{"city_name":"蚌埠"}},"row":"20228352"}
        {"cell":{"colFmly":{"city_name":"南京"}},"row":"15240138"}
        {"cell":{"colFmly":{"city_name":"北京"}},"row":"21320812"}
        {"cell":{"colFmly":{"city_name":"上海"}},"row":"11491261"}
        {"cell":{"colFmly":{"city_name":"上海"}},"row":"19014433"}
        {"cell":{"colFmly":{"city_name":"金华"}},"row":"14510716"}
        {"cell":{"colFmly":{"city_name":"南京"}},"row":"6755573"}
        {"cell":{"colFmly":{"city_name":"杭州"}},"row":"10656101"}
        {"cell":{"colFmly":{"city_name":"武汉"}},"row":"12642084"}
        {"cell":{"colFmly":{"city_name":"深圳"}},"row":"2403242"}
        {"cell":{"colFmly":{"city_name":"沈阳"}},"row":"22377939"}
        {"cell":{"colFmly":{"city_name":"广州"}},"row":"19096787"}
        {"cell":{"colFmly":{"city_name":"成都"}},"row":"17769131"}
        {"cell":{"colFmly":{"city_name":"杭州"}},"row":"11140916"}
        {"cell":{"colFmly":{"city_name":"成都"}},"row":"17821920"}
        {"cell":{"colFmly":{"city_name":"上海"}},"row":"12077858"}
        {"cell":{"colFmly":{"city_name":"苏州"}},"row":"5797375"}*/

        String[] userIds = new String[]{"15042563","15262746","20228352","15240138","19014433","10656101","9672640","19096787","17821920","5797375"} ;

        for(String s:userIds){

        QueryService<UserLabels> userLabelsQueryService = getUserLabelsQueryService();
        UserLabels userLabels = userLabelsQueryService.getT();
        Entity<UserLabels> userLabelsEntity = new Entity<UserLabels>();
        userLabelsEntity.setT(userLabels);
        userLabelsEntity.setColumnFamily(new String[]{"colFmly"});
        userLabelsEntity.setRow(s);

        String jsonParam = JSON.toJSONString(userLabelsEntity);
        String resString = userLabelsQueryService.execute(jsonParam,AppConstants.SVC_TYPE_FIND);
            System.out.println(resString);
        }
        return "***********************************" ;
    }

    /**
     * 根据商家id查询商家表签
     * @return
     * @throws Exception
     */
    public static String findShopLabels() throws Exception{

        /*{"cell":{"colFmly":{"score":"3"}},"row":"1896"}
        {"cell":{"colFmly":{"score":"3"}},"row":"1897"}
        {"cell":{"colFmly":{"score":"3"}},"row":"1898"}
        {"cell":{"colFmly":{"score":"3"}},"row":"1899"}
        {"cell":{"colFmly":{"score":"4"}},"row":"1900"}
        {"cell":{"colFmly":{"score":""}},"row":"1901"}
        {"cell":{"colFmly":{"score":"3"}},"row":"1902"}
        {"cell":{"colFmly":{"score":"3"}},"row":"1903"}
        {"cell":{"colFmly":{"score":"3"}},"row":"1904"}
        {"cell":{"colFmly":{"score":""}},"row":"1905"}
        {"cell":{"colFmly":{"score":""}},"row":"1906"}
        {"cell":{"colFmly":{"score":"4"}},"row":"1907"}
        {"cell":{"colFmly":{"score":"1"}},"row":"1908"}
        {"cell":{"colFmly":{"score":"3"}},"row":"1909"}
        {"cell":{"colFmly":{"score":"0"}},"row":"1910"}*/

        String[] shopIds = new String[]{"1896","1897","1898","1899","1900","1901","1905","1908","1909","1910"} ;

        for(String s:shopIds){
            QueryService<ShopLabels> shopLabelQueryService = getShopLabelsQueryService();
            ShopLabels shopLabels = shopLabelQueryService.getT();
            Entity<ShopLabels> shopLabelsEntity = new Entity<ShopLabels>();
            shopLabelsEntity.setT(shopLabels);
            shopLabelsEntity.setColumnFamily(new String[]{"colFmly"});
            shopLabelsEntity.setRow(s);

            String jsonParam = JSON.toJSONString(shopLabelsEntity);
            String resString = shopLabelQueryService.execute(jsonParam,AppConstants.SVC_TYPE_FIND);

            System.out.println(resString);
        }
        return "+++++++++++++++++++++++++++++" ;
    }

    /**
     * 根据user_id 及 times_tamp区间查询账单
     * @return
     * @throws Exception
     */
    public static String findUserPays() throws Exception{
        QueryService userPayQueryService = new QueryServiceImpl<UserPay>();
        UserPay userPay = new UserPay();;
        userPayQueryService.setT(userPay);

        QueryCondition<UserPay> queryCondition = new QueryCondition<>();
        queryCondition.setT(userPay);
        queryCondition.setTime_stamp_range(new String[]{"2015-11-24 00:00:00","2018-12-24 24:00:00"});
        queryCondition.setUser_id("20621399");
        queryCondition.setColumn_family("colFmly");

        Entity<UserPay> userPayEntity = new Entity<UserPay>();
        userPayEntity.setT(userPay);
        userPayEntity.setQueryCondition(queryCondition);

        String jsonParam = JSON.toJSONString(userPayEntity);
        String resString = userPayQueryService.execute(jsonParam,AppConstants.SVC_TYPE_FIND_ENTITIES);
        return resString ;
    }

    /**
     * 读入user_pay数据导入Hbase（1）
     * @throws Exception
     */
    public static void saveOrUpdate() throws Exception{
        AppUtils.readFile(USER_PAY_URL);
    }

    /**
     * 读入user_pay数据导入Hbase（2）
     * @throws Exception
     */
    public static void dataImport(List<String> lines) throws Exception {

        QueryService userPayQueryService = new QueryServiceImpl<UserPay>();
        UserPay userPay = new UserPay();;
        userPayQueryService.setT(userPay);
        List<Entity> entities = new ArrayList<>() ;
        Entity<UserPay> entity = null ;
        String row = "" ;
        Map<String,Map<String,String>> cell = null;
        Map<String,String> qualifierValueMap = null;
        for (String line:lines){
            qualifierValueMap = new HashMap<>(3);
            String[] values = line.split(",");
            qualifierValueMap.put("user_id",values[0]);
            qualifierValueMap.put("shop_id",values[1]);
            qualifierValueMap.put("time_stamp",values[2]);

            cell = new HashMap<>(1);
            cell.put("colFmly",qualifierValueMap);

            row = values[0]+"_"+values[1]+"_"+values[2].substring(0,10) ;

            entity = new Entity<>() ;
            entity.setCell(cell);
            entity.setRow(row);

            entities.add(entity);
        }

        String jsonParam = JSON.toJSONString(entities);
        String resString = userPayQueryService.execute(jsonParam,AppConstants.SVC_TYPE_SAVE_OR_UPDATE);
    }
}
