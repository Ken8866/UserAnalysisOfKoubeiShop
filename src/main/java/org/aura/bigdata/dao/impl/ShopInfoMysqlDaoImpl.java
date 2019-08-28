package org.aura.bigdata.dao.impl;

import org.aura.bigdata.dao.base.MysqlDaoBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShopInfoMysqlDaoImpl extends MysqlDaoBase {


    public static void main(String[] args) {
        ShopInfoMysqlDaoImpl mysqlDao = new ShopInfoMysqlDaoImpl();
        String exestatus = null;
        try {
            exestatus = mysqlDao.insert("1", "1", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(exestatus);
    }

    public void query() throws Exception{
        Connection conn = getConn();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("select *  from user_pay ");
        while (resultSet.next()){
            String shopId = resultSet.getString("shop_id");
            String cityName = resultSet.getString("city_name");
            System.out.println(shopId+","+cityName);
        }
        statement.close();
        closeConn();
    }

    public String insert(String userId,String shop_id,String time_stamp) throws Exception{
        String execStatus = "sql execute fail !!!" ;
        Connection conn = getConn();
        String sql = "insert into user_pay(user_id,shop_id,time_stamp) values(?,?,?) ";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1,userId);
        preparedStatement.setString(2,shop_id);
        preparedStatement.setString(3,time_stamp);
        int i = preparedStatement.executeUpdate();
        return String.valueOf(i) ;
    }
}
