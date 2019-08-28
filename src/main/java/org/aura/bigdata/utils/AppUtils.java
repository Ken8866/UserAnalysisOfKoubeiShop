package org.aura.bigdata.utils;

import org.apache.commons.io.FileUtils;
import org.aura.bigdata.model.ShopInfo;

import java.io.*;
import java.util.*;

public class AppUtils {

    public static Map<String,ShopInfo> shopInfoMap = new HashMap<>() ;
    private static boolean stopReadFile = false;

    public static void main(String[] args) throws Exception {
//        Properties properties = getProperties("hbase.properties");
//        System.out.println(properties.getProperty("prop.test"));
        readFile(AppConstants.USER_PAY_PATH);
    }

    public static Properties getProperties(String file) throws Exception{
        InputStream in = ClassLoader.getSystemResourceAsStream(file);
        Properties prop = new Properties();
        prop.load(in);
        return prop ;
    }

    public static List<String> readFile(String filePath) throws Exception {
        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = "" ;
        int size = 0 ;
        List<String> lines = new ArrayList<>(10000);
        while( (line = br.readLine()) !=null ){
            lines.add(line);
            size++ ;
            if(size==10000){
                waitProcess(lines);
                lines.clear();
                size=0 ;
            }
            if(stopReadFile) break;
        };
        br.close();
        fr.close();
        return lines ;
    }

    public static void waitProcess(List<String> lines) throws Exception{
        System.out.println("#####################"+lines.size()+"###########################");
//        ServiceClient.dataImport(lines) ;
        printMostUserId(lines);
        stopReadFile = true ;
    }

    public static void initShopInfo(String filePath) throws Exception{
        File file = new File(filePath) ;
        List<String> list = FileUtils.readLines(file);
        ShopInfo shopInfo = null;
        for(String line:list){
            String[] columns = line.split(",");
            shopInfo = new ShopInfo();
            shopInfo.setShop_id(columns[0]);
            shopInfo.setCity_name(columns[1]);
            shopInfo.setPer_pay(Long.valueOf(columns[3]));
            shopInfoMap.put(shopInfo.getShop_id(),shopInfo);
        }
    }

    public static ShopInfo getShopInfo(String shopId) throws Exception{
        ShopInfo shopInfo = null ;
        if(shopInfoMap.containsKey(shopId)){
            shopInfo = shopInfoMap.get(shopId);
        }
        return  shopInfo ;
    }

    public static void printMostUserId(List<String> lines){
        Map<String,List<String>> userPay = new HashMap();
        for (String line:lines){
            String[] split = line.split(",");
            String userId = split[0] ;
            if(userPay.containsKey(userId)){
                userPay.get(userId).add(line);
            }else{
                ArrayList<String> tmpList = new ArrayList<>();
                tmpList.add(line);
                userPay.put(userId,tmpList);
            }
        }
        stopReadFile = true ;
        Set<Map.Entry<String, List<String>>> entries = userPay.entrySet();
        String mostUserId = "" ;
        List<String> mostUserPayList = null;
        long maxSize = 0 ;
        for (Map.Entry<String, List<String>> entry:entries){
            String  userId = entry.getKey();
            List<String> value = entry.getValue();
            if (maxSize<value.size()){
                maxSize = value.size();
                mostUserId = userId ;
                mostUserPayList = value ;
            }
        }
        System.out.println("mostUserId: "+mostUserId+",size: "+maxSize);
    }
}
