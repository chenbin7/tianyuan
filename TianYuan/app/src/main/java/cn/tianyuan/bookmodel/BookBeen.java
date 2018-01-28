package cn.tianyuan.bookmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenbin on 2017/12/2.
 * 书籍
 */


public class BookBeen implements Parcelable {

    public String id;
    public String userId;
    public String typeId;
    public String fraviteId;
    public String name;
    public String descriptor;
    public int price;
    public long addTime;
    public String picture;

    public BookBeen(){}

    protected BookBeen(Parcel in) {
        id = in.readString();
        userId = in.readString();
        typeId = in.readString();
        name = in.readString();
        descriptor = in.readString();
        price = in.readInt();
        addTime = in.readLong();
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
        dest.writeString(userId);
        dest.writeString(typeId);
        dest.writeString(name);
        dest.writeString(descriptor);
        dest.writeInt(price);
        dest.writeLong(addTime);
        dest.writeString(picture);
    }
}
