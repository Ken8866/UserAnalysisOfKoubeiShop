package org.aura.bigdata.spark.Streaming;

import com.alibaba.fastjson.JSON;
import org.aura.bigdata.JavaRedisClient;
import org.aura.bigdata.view.vo.ShopTrans;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StreamingAnalysisResult {

    private static String countResultByShop = null ;

    private static String countResultBycity = null ;

    public static void main(String[] args) throws Exception{
        StreamingAnalysisResult realTimeAnalysis = new StreamingAnalysisResult();

        realTimeAnalysis.getShopCountByShop();
        realTimeAnalysis.getCountResultByCity();

        System.out.println(countResultBycity);
        System.out.println(countResultByShop);
    }

    /**
     * 查看按商家统计的结果
     * @throws Exception
     */
    public void getShopCountByShop() throws Exception{
        Jedis jedis = JavaRedisClient.getJedisPool().getResource();
        Set<String> jiaoyi = jedis.keys("jiaoyi*");

        List<ShopTrans> trans = new ArrayList<>();
        ShopTrans shopTrans = null;



        for(String shoptrans: jiaoyi){
            shopTrans = new ShopTrans();
            shopTrans.setShopId(shoptrans.substring(6));
            shopTrans.setCount(jedis.get(shoptrans));
            trans.add(shopTrans);
        }

        this.countResultByShop = JSON.toJSONString(trans);
    }

    /**
     * 查看按城市统计的结果
     * @throws Exception
     */
    public void getCountResultByCity() throws Exception{
        Jedis jedis = JavaRedisClient.getJedisPool().getResource();
        Set<String> jiaoyi = jedis.keys("交易*");

        List<ShopTrans> trans = new ArrayList<>();
        ShopTrans shopTrans = null;
        for(String shoptrans: jiaoyi){
            shopTrans = new ShopTrans();
            shopTrans.setShopId(shoptrans.substring(2));
            shopTrans.setCount(jedis.get(shoptrans));
            trans.add(shopTrans);
        }

        this.countResultBycity = JSON.toJSONString(trans);
    }

    public String getCountResultByShop() {
        return countResultByShop;
    }

    public void setCountResultByShop(String countResultByShop) {
        this.countResultByShop = countResultByShop;
    }

    public String getCountResultBycity() {
        return countResultBycity;
    }

    public void setCountResultBycity(String countResultBycity) {
        this.countResultBycity = countResultBycity;
    }
}
