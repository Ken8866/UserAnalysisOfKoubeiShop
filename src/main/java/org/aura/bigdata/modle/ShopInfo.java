package org.aura.bigdata.modle;

import java.io.Serializable;

public class ShopInfo implements Serializable {

    private String shopId;

    private String cityName;

    private String locationId;

    private double perPay;

    private double score;

    private double commentCnt;

    private double shopLevel;

    private String cateName_1;

    private String cateName_2;

    private String cateName_3;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public double getPerPay() {
        return perPay;
    }

    public void setPerPay(double perPay) {
        this.perPay = perPay;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(double commentCnt) {
        this.commentCnt = commentCnt;
    }

    public double getShopLevel() {
        return shopLevel;
    }

    public void setShopLevel(double shopLevel) {
        this.shopLevel = shopLevel;
    }

    public String getCateName_1() {
        return cateName_1;
    }

    public void setCateName_1(String cateName_1) {
        this.cateName_1 = cateName_1;
    }

    public String getCateName_2() {
        return cateName_2;
    }

    public void setCateName_2(String cateName_2) {
        this.cateName_2 = cateName_2;
    }

    public String getCateName_3() {
        return cateName_3;
    }

    public void setCateName_3(String cateName_3) {
        this.cateName_3 = cateName_3;
    }

    public ShopInfo(String shopId, String cityName, String locationId, double perPay, double score, double commentCnt, double shopLevel, String cateName_1, String cateName_2, String cateName_3) {
        this.shopId = shopId;
        this.cityName = cityName;
        this.locationId = locationId;
        this.perPay = perPay;
        this.score = score;
        this.commentCnt = commentCnt;
        this.shopLevel = shopLevel;
        this.cateName_1 = cateName_1;
        this.cateName_2 = cateName_2;
        this.cateName_3 = cateName_3;
    }

    public ShopInfo() {
    }

    public static ShopInfo parseShopInfo(String str) {
        String[] fields = str.split(",");
        if (fields.length < 9) {
            throw new IllegalArgumentException("Each line must contain 10 fields");
        }
        String shopId = fields[0];
        String cityName = fields[1];
        String locationId = fields[2];
        double perPay = Double.valueOf("".equals(fields[3])?"0.0":fields[3]);
        double score = Double.valueOf("".equals(fields[4])?"0.0":fields[4]);
        double commentCnt = Double.valueOf("".equals(fields[5])?"0.0":fields[5]);
        double shopLevel = Double.valueOf("".equals(fields[6])?"0.0":fields[6]);
        String cateName_1 = fields[7];
        String cateName_2 = fields[8];
        String cateName_3 = "";
        if(fields.length>9){
           cateName_3= fields[9];
        }
        return new ShopInfo(shopId, cityName, locationId,perPay,score,commentCnt,shopLevel,cateName_1,cateName_2,cateName_3);
    }
}
