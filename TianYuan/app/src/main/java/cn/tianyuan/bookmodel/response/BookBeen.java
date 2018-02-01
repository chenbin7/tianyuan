package cn.tianyuan.bookmodel.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by chenbin on 2017/12/2.
 * 书籍
 */


public class BookBeen implements Parcelable {

    public String id;
    public String userid;
    public String typeid;
    public String fraviteid;
    public String name;
    public String descriptor;
    public int price;
    public int storesum;
    public int sellsum;
    public long addtime;
    public String picture;

    public BookBeen(){}

    public String toString(){
        return "book:"+name+"  "+picture+"  "+descriptor+"  "+id+"  "+userid+"  "+typeid+"  "+storesum+"  "+sellsum+"  "+fraviteid;
    }

    protected BookBeen(Parcel in) {
        id = in.readString();
        userid = in.readString();
        typeid = in.readString();
        fraviteid = in.readString();
        name = in.readString();
        descriptor = in.readString();
        price = in.readInt();
        storesum = in.readInt();
        sellsum = in.readInt();
        addtime = in.readLong();
        picture = in.readString();
    }

    public static final Creator<BookBeen> CREATOR = new Creator<BookBeen>() {
        @Override
        public BookBeen createFromParcel(Parcel in) {
            return new BookBeen(in);
        }

        @Override
        public BookBeen[] newArray(int size) {
            return new BookBeen[size];
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
        dest.writeString(typeid);
        dest.writeString(fraviteid);
        dest.writeString(name);
        dest.writeString(descriptor);
        dest.writeInt(price);
        dest.writeInt(storesum);
        dest.writeInt(sellsum);
        dest.writeLong(addtime);
        dest.writeString(picture);
    }
}
