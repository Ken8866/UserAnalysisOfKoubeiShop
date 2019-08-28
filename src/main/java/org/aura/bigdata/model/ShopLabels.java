package org.aura.bigdata.model;

import java.io.Serializable;

public class ShopLabels implements Serializable {
    private String last_7_day_reviewed ;
    private String last_1_month_reviewed ;
    private String last_3_month_reviewed ;

    private String last_7_day_payed ;
    private String last_1_month_payed ;
    private String last_3_month_payed ;

    private String score ;

    private String shop_id ;

    public String getLast_7_day_reviewed() {
        return last_7_day_reviewed;
    }

    public void setLast_7_day_reviewed(String last_7_day_reviewed) {
        this.last_7_day_reviewed = last_7_day_reviewed;
    }

    public String getLast_1_month_reviewed() {
        return last_1_month_reviewed;
    }

    public void setLast_1_month_reviewed(String last_1_month_reviewed) {
        this.last_1_month_reviewed = last_1_month_reviewed;
    }

    public String getLast_3_month_reviewed() {
        return last_3_month_reviewed;
    }

    public void setLast_3_month_reviewed(String last_3_month_reviewed) {
        this.last_3_month_reviewed = last_3_month_reviewed;
    }

    public String getLast_7_day_payed() {
        return last_7_day_payed;
    }

    public void setLast_7_day_payed(String last_7_day_payed) {
        this.last_7_day_payed = last_7_day_payed;
    }

    public String getLast_1_month_payed() {
        return last_1_month_payed;
    }

    public void setLast_1_month_payed(String last_1_month_payed) {
        this.last_1_month_payed = last_1_month_payed;
    }

    public String getLast_3_month_payed() {
        return last_3_month_payed;
    }

    public void setLast_3_month_payed(String last_3_month_payed) {
        this.last_3_month_payed = last_3_month_payed;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }
}
