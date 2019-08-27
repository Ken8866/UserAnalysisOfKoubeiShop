package org.aura.bigdata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.aura.bigdata.dao.base.HbaseDaoBase;
import org.aura.bigdata.model.*;
import org.aura.bigdata.service.QueryService;
import org.aura.bigdata.service.impl.QueryServiceImpl;
import org.aura.bigdata.utils.QueryCondition;
import scala.App;

import java.io.FileReader;
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
     * 服务调用方法入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        String resString = findShopLabels();
        System.out.println(resString);
}
    public static QueryService getUserLabelsQueryService() throws Exception {
        QueryService userLabelsQueryService = new QueryServiceImpl<UserLabels>();
        UserLabels userLabels = new UserLabels();;
        userLabelsQueryService.setT(userLabels);
        return  userLabelsQueryService ;
    }

    public static QueryService getShopLabelsQueryService() throws Exception {
        QueryService shopLabelsQueryService = new QueryServiceImpl<ShopLabels>();
        ShopLabels shopLabels = new ShopLabels();;
        shopLabelsQueryService.setT(shopLabels);
        return  shopLabelsQueryService ;
    }

    public static QueryService getUserPayQueryService() throws Exception {
        QueryService userPayQueryService = new QueryServiceImpl<UserPay>();
        UserPay userPay = new UserPay();;
        userPayQueryService.setT(userPay);
        return  userPayQueryService ;
    }

    public static String countUserPay() throws Exception{

        QueryService<UserPay> userPayQueryService = getUserPayQueryService();
        UserPay userPay = userPayQueryService.getT();
        Entity<UserPay> userPayEntity = new Entity<UserPay>();
        userPayEntity.setT(userPay);
//        userPayEntity.setColumnFamily(new String[]{"colFmly"});
        String jsonParam = JSON.toJSONString(userPayEntity);
        String resString = userPayQueryService.execute(jsonParam,AppConstants.SVC_TYPE_COUNT);
//        String resString = userPayQueryService.execute(jsonParam,AppConstants.SVC_TYPE_CREATE_TABLE);

        return resString;
    }

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
     * 根据用户id获取用户标签
     * @return
     * @throws Exception
     */
    public static String findUserLabels() throws Exception{
        QueryService<UserLabels> userLabelsQueryService = getUserLabelsQueryService();
        UserLabels userLabels = userLabelsQueryService.getT();
        Entity<UserLabels> userLabelsEntity = new Entity<UserLabels>();
        userLabelsEntity.setT(userLabels);
        userLabelsEntity.setColumnFamily(new String[]{"colFmly"});
        userLabelsEntity.setRow("20314925");

        String jsonParam = JSON.toJSONString(userLabelsEntity);
        String resString = userLabelsQueryService.execute(jsonParam,AppConstants.SVC_TYPE_FIND);
        return resString ;
    }

    /**
     * 根据商家id获取商家表签
     * @return
     * @throws Exception
     */
    public static String findShopLabels() throws Exception{
        QueryService<ShopLabels> shopLabelQueryService = getShopLabelsQueryService();
        ShopLabels shopLabels = shopLabelQueryService.getT();
        Entity<ShopLabels> shopLabelsEntity = new Entity<ShopLabels>();
        shopLabelsEntity.setT(shopLabels);
        shopLabelsEntity.setColumnFamily(new String[]{"colFmly"});
        shopLabelsEntity.setRow("1852");

        String jsonParam = JSON.toJSONString(shopLabelsEntity);
        String resString = shopLabelQueryService.execute(jsonParam,AppConstants.SVC_TYPE_FIND);
        return resString ;
    }

    /**
     * find user pay by condition
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
     * count user view example not implement
     * @return
     * @throws Exception
     */
    public static String countUserView() throws Exception{
        QueryService userViewQueryService = new QueryServiceImpl<UserView>();
        UserView userView = new UserView();;
        userViewQueryService.setT(userView);
        Entity<UserView> userViewEntity = new Entity<UserView>();
        userViewEntity.setT(userView);

        String jsonParam = JSON.toJSONString(userViewEntity);
//        String resString = userViewQueryService.execute(jsonParam,AppConstants.SVC_TYPE_COUNT);
        return null;
    }

    /**
     * read file data and import to hbase
     * @throws Exception
     */
    public static void saveOrUpdate() throws Exception{
        AppUtils.readFile(USER_PAY_URL);
    }

    /**
     * import data to hbase
     * @param lines
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
