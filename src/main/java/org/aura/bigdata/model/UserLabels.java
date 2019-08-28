package org.aura.bigdata.model;

import java.io.Serializable;

public class UserLabels implements Serializable {

    private String last_7_day_review ;
    private String last_1_month_review ;
    private String last_3_month_review ;

    private String last_7_day_pay ;
    private String last_1_month_pay ;
    private String last_3_month_pay ;

    private String city_name ;

    private String user_id;

    public String getLast_7_day_review() {
        return last_7_day_review;
    }

    public void setLast_7_day_review(String last_7_day_review) {
        this.last_7_day_review = last_7_day_review;
    }

    public String getLast_1_month_review() {
        return last_1_month_review;
    }

    public void setLast_1_month_review(String last_1_month_review) {
        this.last_1_month_review = last_1_month_review;
    }

    public String getLast_3_month_review() {
        return last_3_month_review;
    }

    public void setLast_3_month_review(String last_3_month_review) {
        this.last_3_month_review = last_3_month_review;
    }

    public String getLast_7_day_pay() {
        return last_7_day_pay;
    }

    public void setLast_7_day_pay(String last_7_day_pay) {
        this.last_7_day_pay = last_7_day_pay;
    }

    public String getLast_1_month_pay() {
        return last_1_month_pay;
    }

    public void setLast_1_month_pay(String last_1_month_pay) {
        this.last_1_month_pay = last_1_month_pay;
    }

    public String getLast_3_month_pay() {
        return last_3_month_pay;
    }

    public void setLast_3_month_pay(String last_3_month_pay) {
        this.last_3_month_pay = last_3_month_pay;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
