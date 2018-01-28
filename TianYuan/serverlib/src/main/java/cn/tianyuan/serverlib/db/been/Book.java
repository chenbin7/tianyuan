package cn.tianyuan.serverlib.db.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenbin on 2017/12/2.
 * 书籍
 */


@Entity
public class Book {

    @Id
    public String id;
    public String userId;
    public String typeId;
    public String name;
    public String descriptor;
    public int price;
    public int sellSum;
    public int storeSum;
    public long addTime;
    public String picture;
    @Generated(hash = 191552555)
    public Book(String id, String userId, String typeId, String name,
            String descriptor, int price, int sellSum, int storeSum, long addTime,
            String picture) {
        this.id = id;
        this.userId = userId;
        this.typeId = typeId;
        this.name = name;
        this.descriptor = descriptor;
        this.price = price;
        this.sellSum = sellSum;
        this.storeSum = storeSum;
        this.addTime = addTime;
        this.picture = picture;
    }
    @Generated(hash = 1839243756)
    public Book() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTypeId() {
        return this.typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescriptor() {
        return this.descriptor;
    }
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getSellSum() {
        return this.sellSum;
    }
    public void setSellSum(int sellSum) {
        this.sellSum = sellSum;
    }
    public int getStoreSum() {
        return this.storeSum;
    }
    public void setStoreSum(int storeSum) {
        this.storeSum = storeSum;
    }
    public long getAddTime() {
        return this.addTime;
    }
    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
    public String getPicture() {
        return this.picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }


}
