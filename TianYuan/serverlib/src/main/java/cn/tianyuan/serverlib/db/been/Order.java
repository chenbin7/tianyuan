package cn.tianyuan.serverlib.db.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenbin on 2017/12/2.
 * 单条订单
 */

@Entity
public class Order {

    @Id
    public String id;
    public long time;
    public String addrId;
    public String userId;
    public int totalPrice;
    @Generated(hash = 1324123749)
    public Order(String id, long time, String addrId, String userId,
            int totalPrice) {
        this.id = id;
        this.time = time;
        this.addrId = addrId;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }
    @Generated(hash = 1105174599)
    public Order() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getAddrId() {
        return this.addrId;
    }
    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getTotalPrice() {
        return this.totalPrice;
    }
    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }


}
    