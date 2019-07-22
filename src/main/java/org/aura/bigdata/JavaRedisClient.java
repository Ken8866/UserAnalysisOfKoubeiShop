package org.aura.bigdata;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;

public class JavaRedisClient implements Serializable {

    private static JedisPool pool = null;

    public static JedisPool getJedisPool(){
        if(pool == null){
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(200);
            config.setTestOnBorrow(true);
            pool = new JedisPool(config,"bigdata",6379,20000);
        }
        return  pool ;
    }

    public static void main(String[] args) {
        Jedis jedis = getJedisPool().getResource();
        jedis.set("testkey","testvalue");
        jedis.close();
    }


}
