package org.aura.bigdata.view.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class ShopTrans implements Serializable {

    private String shopId ;
    private String count ;
    private String city ;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("shopId", shopId)
                .append("count", count)
                .append("city", city)
                .toString();
    }

}
