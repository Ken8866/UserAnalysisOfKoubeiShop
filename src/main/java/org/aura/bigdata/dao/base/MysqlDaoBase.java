package org.aura.bigdata.dao.base;

import org.apache.tools.ant.taskdefs.EchoXML;

import java.sql.Connection;
import java.sql.DriverManager;

public class MysqlDaoBase {

    public static final String url = "jdbc:mysql://192.168.23.129/co_db";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "root";
    private static Connection conn = null ;

    public static Connection getConn() throws Exception {
        Class.forName(name);
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static void closeConn() throws Exception{
        if(conn!=null){
            conn.close();
        }
    }
}
