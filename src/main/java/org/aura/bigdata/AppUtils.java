package org.aura.bigdata;

import org.apache.commons.io.FileUtils;
import org.aura.bigdata.model.ShopInfo;

import java.io.*;
import java.util.*;

public class AppUtils {

    public static Map<String,ShopInfo> shopInfoMap = new HashMap<>() ;

    public static void main(String[] args) {
        try {
            Properties properties = getProperties("hbase.properties");
            String o = properties.getProperty("prop.test");
            initShopInfo(AppConstants.SHOP_INFO_PATH);
            Set<Map.Entry<String, ShopInfo>> entries = shopInfoMap.entrySet();
            for (Map.Entry<String, ShopInfo> obj:entries){
                System.out.println(obj.getKey()+"->"+obj.getValue().getPer_pay());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        };
        br.close();
        fr.close();
        return lines ;
    }
    public static void waitProcess(List<String> lines) throws Exception{
        System.out.println("#####################"+lines.size()+"###########################");
        ServiceClient.dataImport(lines) ;
        System.out.println(lines.get(lines.size()-1));
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
}
