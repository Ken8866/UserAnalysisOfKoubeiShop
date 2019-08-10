package org.aura.bigdata.modle;



import java.io.Serializable;
import java.sql.Timestamp;

public class UserPay implements Serializable {

    private String userId;

    private String shopId;

    private Timestamp timeStamp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public UserPay() {
    }

    public UserPay(String userId, String shopId, Timestamp timeStamp) {
        this.userId = userId;
        this.shopId = shopId;
        this.timeStamp = timeStamp;
    }

    public static UserPay parseUserPay(String str) {
        String[] fields = str.split(",");
        if (fields.length != 3) {
            throw new IllegalArgumentException("Each line must contain 3 fields");
        }
        String userId = fields[0];
        String shopId = fields[1];
        Timestamp timestamp = Timestamp.valueOf(fields[2]);
        return new UserPay(userId, shopId, timestamp);
    }
}
