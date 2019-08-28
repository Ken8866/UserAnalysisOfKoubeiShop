package org.aura.bigdata.dao.base;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.Serializable;

public class HbaseDaoBase implements Serializable {

    private Connection conn = null ;
    private Table table = null ;
    private static Configuration conf = null;
    private Admin admin = null ;

    /**
     * init Hbase Configuration
     */
    public static void initConf() throws Exception{
        conf = HBaseConfiguration.create();
        conf.addResource("hbase-site.xml");
    }

    public static void initConf(String zkUrl,String hbaseRoot) throws Exception{
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum",zkUrl);
        conf.set("zookeeper.znode.parent", hbaseRoot);
    }

    /**
     * get Admin
     * @return
     * @throws Exception
     */
    public Admin getAdmin() throws Exception{
        conn = getConn();
        admin = conn.getAdmin();
        return admin ;
    }

    /**
     * get connection
     * @return
     * @throws Exception
     */
    public Connection getConn() throws Exception{
        conn = ConnectionFactory.createConnection(conf);
        return conn;
    }

    /**
     * close connection
     * @throws Exception
     */
    public void closeConn() throws Exception{
       if(conn!=null) conn.close();
    }

    /**
     * close table
     * @throws Exception
     */
    public void closeTable() throws Exception{
        if(table!=null) table.close();
    }
    /**
     * get a table
     * @param tableName
     * @return
     * @throws Exception
     */
    public Table getTable(String tableName) throws Exception{
        conn = getConn();
        table = conn.getTable(TableName.valueOf(tableName));
        return table ;
    }

    /**
     * create a table
     * @param tableName
     * @param familyNames
     * @throws Exception
     */
    public void  createTable(String tableName,String[] familyNames) throws Exception{
        admin = getAdmin();
        if(!admin.tableExists(TableName.valueOf(tableName))){
            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            for (String fn:familyNames) {
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(Bytes.toBytes(fn));
                tableDescriptor.addFamily(columnDescriptor);
            }
            admin.createTable(tableDescriptor);
        }
        admin.close();
    }

    /**
     * delete a table
     * @param tableName
     * @throws Exception
     */
    public void deleteTable(String tableName) throws Exception{
        admin = getAdmin();
        if(admin.tableExists(TableName.valueOf(tableName))){
            if(admin.isTableDisabled(TableName.valueOf(tableName))){
                admin.disableTable(TableName.valueOf(tableName));
            }
            admin.deleteTable(TableName.valueOf(tableName));
        }
        admin.close();
    }

}
