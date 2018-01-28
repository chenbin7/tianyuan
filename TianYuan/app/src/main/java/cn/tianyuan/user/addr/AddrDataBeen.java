package cn.tianyuan.user.addr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/10/24.
 */

public class AddrDataBeen implements Parcelable {
    public String addressId;     //地址ID
    public String pName;
    public String cityName;
    public String adName;
    public String address;       //地址
    public String communityName; //小区名称
    public String detail;         //座
    public String fullAddress;   //完整地址

    public String toString(){return addressId+","+address+","+communityName+","+detail+","+fullAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addressId);
        dest.writeString(pName);
        dest.writeString(cityName);
        dest.writeString(adName);
        dest.writeString(address);
        dest.writeString(communityName);
        dest.writeString(detail);
        dest.writeString(fullAddress);
    }

    public static final Creator<AddrDataBeen> CREATOR = new Creator<AddrDataBeen>(){

        @Override
        public AddrDataBeen createFromParcel(Parcel source) {
            AddrDataBeen addr = new AddrDataBeen();
            addr.addressId     = source.readString();
            addr.pName     = source.readString();
            addr.cityName     = source.readString();
            addr.adName     = source.readString();
            addr.address       = source.readString();
            addr.communityName = source.readString();
            addr.detail = source.readString();
            addr.fullAddress   = source.readString();
            return addr;
        }

        @Override
        public AddrDataBeen[] newArray(int size) {
            return new AddrDataBeen[size];
        }
    };
}
