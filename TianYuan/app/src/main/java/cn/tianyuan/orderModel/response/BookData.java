package cn.tianyuan.orderModel.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/26.
 * 商品信息
 */

public class BookData implements Parcelable {
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

    protected BookData(Parcel in) {
        id = in.readString();
        userId = in.readString();
        typeId = in.readString();
        intentId = in.readString();
        name = in.readString();
        descriptor = in.readString();
        price = in.readInt();
        sellsum = in.readInt();
        storesum = in.readInt();
        count = in.readInt();
        addTime = in.readLong();
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
        dest.writeString(userId);
        dest.writeString(typeId);
        dest.writeString(intentId);
        dest.writeString(name);
        dest.writeString(descriptor);
        dest.writeInt(price);
        dest.writeInt(sellsum);
        dest.writeInt(storesum);
        dest.writeInt(count);
        dest.writeLong(addTime);
        dest.writeString(picture);
        dest.writeByte((byte) (isChoosed ? 1 : 0));
    }
}
