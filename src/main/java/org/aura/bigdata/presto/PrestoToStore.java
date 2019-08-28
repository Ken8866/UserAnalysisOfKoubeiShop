package org.aura.bigdata.presto;

import java.sql.*;


public class PrestoToStore {

    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        Class.forName("com.facebook.presto.jdbc.PrestoDriver");
        Connection connection = DriverManager.getConnection("jdbc:presto://bigdata:8080/hive/koubei","bigdata",null);  ;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select count(user_id), date_format(time_stamp,'%Y-%m-%d') as date, shop_id from user_pay group by date_format(time_stamp,'%Y-%m-%d'), shop_id");
        while (rs.next()) {
            System.out.println(rs.getString(1)+',' +rs.getString(2));
        }
        rs.close();
        connection.close();

    }
}
