package cn.tianyuan.serverlib.db.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenbin on 2017/12/2.
 * 购物车里的单条数据
 */

@Entity
public class Intention {

    @Id
    public String id;
    public String bookId;
    public String userId;
    public String orderId;
    public int count;
    @Generated(hash = 1175044957)
    public Intention(String id, String bookId, String userId, String orderId,
            int count) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.orderId = orderId;
        this.count = count;
    }
    @Generated(hash = 1673574624)
    public Intention() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getOrderId() {
        return this.orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }

}
