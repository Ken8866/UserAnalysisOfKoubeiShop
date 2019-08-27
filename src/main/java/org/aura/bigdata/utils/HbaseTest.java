package org.aura.bigdata.utils;

import com.alibaba.fastjson.JSON;
import org.aura.bigdata.dao.impl.EntityDaoImpl;
import org.aura.bigdata.model.Entity;

import java.util.HashMap;
import java.util.Map;

public class HbaseTest {

    public static void main(String[] args) throws Exception{
        EntityDaoImpl entityDao = new EntityDaoImpl<>();
        entityDao.initConf("bigdata","/hbase");
        Entity entity = new Entity();
        entity.setRow("10000");
        entity.setName("userlabels");

        HashMap<String, Map<String,String>> cell = new HashMap<>(1);
        Map<String,String> qualifierValueMap = new HashMap<String,String>();
        qualifierValueMap.put("col1","1");
        cell.put("colFmly",qualifierValueMap);
        entity.setCell(cell);

        System.out.println(JSON.toJSONString(entity));
        entityDao.saveOrUpdate(entity);

    }

}
