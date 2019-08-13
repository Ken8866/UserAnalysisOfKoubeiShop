package org.aura.bigdata.utils;

import java.io.Serializable;

public class QueryCondition<T> implements Serializable {

    private T t;
    private String[] time_stamp_range ;
    private String user_id ;
    private String column_family ;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String[] getTime_stamp_range() {
        return time_stamp_range;
    }

    public void setTime_stamp_range(String[] time_stamp_range) {
        this.time_stamp_range = time_stamp_range;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getColumn_family() {
        return column_family;
    }

    public void setColumn_family(String column_family) {
        this.column_family = column_family;
    }
}
