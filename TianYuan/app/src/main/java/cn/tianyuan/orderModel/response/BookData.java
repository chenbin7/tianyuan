package cn.tianyuan.orderModel.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/26.
 * 商品信息
 */

public class BookData implements Parcelable {
    public String id;
    public String userid;
    public String type;
    public String intentId;
    public String name;
    public String descriptor;
    public int price;
    public int sellsum;
    public int storesum;
    public int count;
    public long addtime;
    public String picture;
    public boolean isChoosed;

    public BookData(String id, String userId, String type, String name, String descriptor, int price,int count, long addTime, String picture) {
        this.id = id;
        this.userid = userId;
        this.type = type;
        this.name = name;
        this.descriptor = descriptor;
        this.price = price;
        this.count = count;
        this.addtime = addTime;
        this.picture = picture;
        isChoosed = false;
    }

    public String toString(){
        return id+","+userid+","+type+","+intentId+","+name+"."+price+","+count+","+descriptor+","+picture;
    }

    protected BookData(Parcel in) {
        id = in.readString();
        userid = in.readString();
        type = in.readString();
        intentId = in.readString();
        name = in.readString();
        descriptor = in.readString();
        price = in.readInt();
        sellsum = in.readInt();
        storesum = in.readInt();
        count = in.readInt();
        addtime = in.readLong();
        picture = in.readString();
        isChoosed = in.readByte() != 0;
    }

    public static final Creator<BookData> CREATOR = new Creator<BookData>() {
        @Override
        public BookData createFromParcel(Parcel in) {
            return new BookData(in);
        }

        @Override
        public BookData[] newArray(int size) {
            return new BookData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userid);
        dest.writeString(type);
        dest.writeString(intentId);
        dest.writeString(name);
        dest.writeString(descriptor);
        dest.writeInt(price);
        dest.writeInt(sellsum);
        dest.writeInt(storesum);
        dest.writeInt(count);
        dest.writeLong(addtime);
        dest.writeString(picture);
        dest.writeByte((byte) (isChoosed ? 1 : 0));
    }
}
