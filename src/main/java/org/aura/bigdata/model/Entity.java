package org.aura.bigdata.model;

import org.aura.bigdata.utils.QueryCondition;

import java.io.Serializable;
import java.util.Map;

public class Entity<T> implements Serializable {

    private String name = null ;
    private String row = null;
    private Map<String,Map<String,String>> cell = null;
    private Map<String,String> columns = null ;
    private String[] columnFamily = null ;
    private T t = null ;
    private ShopInfo shopInfo ;
    private QueryCondition<T> queryCondition = null ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public Map<String, Map<String, String>> getCell() {
        return cell;
    }

    public void setCell(Map<String, Map<String, String>> cell) {
        this.cell = cell;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public String[] getColumnFamily() {
        return columnFamily;
    }

    public void setColumnFamily(String[] columnFamily) {
        this.columnFamily = columnFamily;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public QueryCondition<T> getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(QueryCondition<T> queryCondition) {
        this.queryCondition = queryCondition;
    }
}
