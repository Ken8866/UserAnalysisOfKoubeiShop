package org.aura.bigdata.view.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class UserBill implements Serializable {

    private String userId = "";
    private String timestamp = "" ;
    private String shopId = "";
    private String perPay = "" ;
    private String cityName = "" ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPerPay() {
        return perPay;
    }

    public void setPerPay(String perPay) {
        this.perPay = perPay;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("timestamp", timestamp)
                .append("shopId", shopId)
                .append("perPay", perPay)
                .append("cityName", cityName)
                .toString();
    }


}
