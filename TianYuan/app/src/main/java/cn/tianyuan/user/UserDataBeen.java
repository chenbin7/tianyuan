package cn.tianyuan.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/10/25.
 */

public class UserDataBeen implements Parcelable {
    public String userId;
    public String userName;
    public String userHeadPic;
    public String telephone;
    public String sex;



    public String toString(){
        return userId+","+userName+","+userHeadPic+","+telephone+","+sex;
    }

    public String getUserName(){
        return userName;
    }

    public String getAccount(){
        if(telephone == null)
            return "";
        return telephone;
    }

    public String getUserHeadPic() {
        return userHeadPic;
    }

    protected UserDataBeen(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        userHeadPic = in.readString();
        telephone = in.readString();
        sex = in.readString();
    }

    public static final Creator<UserDataBeen> CREATOR = new Creator<UserDataBeen>() {
        @Override
        public UserDataBeen createFromParcel(Parcel in) {
            return new UserDataBeen(in);
        }

        @Override
        public UserDataBeen[] newArray(int size) {
            return new UserDataBeen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userHeadPic);
        dest.writeString(telephone);
        dest.writeString(sex);
    }
}
