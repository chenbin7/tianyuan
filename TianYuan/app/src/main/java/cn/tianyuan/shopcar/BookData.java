package cn.tianyuan.shopcar;

/**
 * Created by Administrator on 2017/3/26.
 * 商品信息
 */

public class BookData {
    public String id;
    public String userId;
    public String typeId;
    public String intentId;
    public String name;
    public String descriptor;
    public int price;
    public int sellsum;
    public int storesum;
    public int count;
    public long addTime;
    public String picture;
    public boolean isChoosed;

    public BookData(String id, String userId, String typeId, String name, String descriptor, int price,int count, long addTime, String picture) {
        this.id = id;
        this.userId = userId;
        this.typeId = typeId;
        this.name = name;
        this.descriptor = descriptor;
        this.price = price;
        this.count = count;
        this.addTime = addTime;
        this.picture = picture;
        isChoosed = false;
    }

}
