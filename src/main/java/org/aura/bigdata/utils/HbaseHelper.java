package org.aura.bigdata.utils;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.aura.bigdata.dao.impl.EntityDaoImpl;
import org.aura.bigdata.model.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HbaseHelper implements Serializable {


    public static void main(String[] args) throws Exception{
        get(null);
    }

    public static void testConn() throws Exception{
        EntityDaoImpl entityDao = new EntityDaoImpl<>();
        entityDao.initConf("bigdata","/hbase");
        Entity entity = new Entity();
        entity.setRow("0001");
        entity.setName("userlabels");

        HashMap<String, Map<String,String>> cell = new HashMap<>(1);
        Map<String,String> qualifierValueMap = new HashMap<String,String>();
        qualifierValueMap.put("col","v2");
        cell.put("colFmly",qualifierValueMap);
        entity.setCell(cell);

        System.out.println(JSON.toJSONString(entity));

        entityDao.saveOrUpdate(entity);

    }


    public static void save(Entity entity) throws Exception{

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","bigdata");
        conf.set("zookeeper.znode.parent", "/hbase");
        Connection conn = ConnectionFactory.createConnection(conf);
        Table userlabels = conn.getTable(TableName.valueOf("userlabels"));
        Table shoplabels = conn.getTable(TableName.valueOf("shoplabels"));



        if(userlabels!=null) userlabels.close();
        if(shoplabels!=null) shoplabels.close();
        conn.close();

    }

    public static void get(Entity entity) throws Exception{

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","hadoopnode");
        conf.set("zookeeper.znode.parent", "/hbase");
        Connection conn = ConnectionFactory.createConnection(conf);
        Table userlabels = conn.getTable(TableName.valueOf("userlabels"));
        Get get = new Get(Bytes.toBytes("5797375"));
        get.addColumn(Bytes.toBytes("colFmly"), Bytes.toBytes("city_name"));
        Result result = userlabels.get(get);

        byte[] bytes = CellUtil.cloneValue(result.listCells().get(0));
        System.out.println(Bytes.toString(bytes));

        if(userlabels!=null) userlabels.close();
        conn.close();

    }

}
