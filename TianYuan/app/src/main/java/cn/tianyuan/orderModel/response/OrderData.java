package cn.tianyuan.orderModel.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/2/5.
 */

public class OrderData implements Parcelable {

    public String id;
    public long time;
    public int price;
    public String addr;
    public String name;
    public String phone;

    protected OrderData(Parcel in) {
        id = in.readString();
        time = in.readLong();
        price = in.readInt();
        addr = in.readString();
        name = in.readString();
        phone = in.readString();
    }

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel in) {
            return new OrderData(in);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(time);
        dest.writeInt(price);
        dest.writeString(addr);
        dest.writeString(name);
        dest.writeString(phone);
    }
}
