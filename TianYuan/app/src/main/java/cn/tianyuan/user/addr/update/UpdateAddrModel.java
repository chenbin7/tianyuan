package cn.tianyuan.user.addr.update;

import android.util.Log;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.Response;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.user.addr.AddrDataBeen;
import cn.tianyuan.user.addr.IAddr;
import cn.tianyuan.common.util.CheckSum;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/25.
 */

public class UpdateAddrModel {

    private String addressId;

    public String getAddressId() {
        return addressId;
    }

    public void addAddr(AddrDataBeen addr, HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("address", addr.address)
                .append("communityName", addr.communityName)
                .append("block", addr.detail)
                .append("fullAddress", addr.fullAddress)
                .getCheckSum();
        Log.i("Model", "addAddr: "+checkSum);
        HttpResource.getInstance()
                .getRetrofit()
                .create(IAddr.class)
                .addAddr(userId,
                        addr.address,
                        addr.pName,
                        addr.cityName,
                        addr.adName,
                        addr.communityName,
                        addr.detail,
                        addr.fullAddress,
                        checkSum,
                        AppProperty.token)
                .subscribe(new Consumer<AddAddrResponse>() {
                    @Override
                    public void accept(AddAddrResponse response) throws Exception {
                        Log.d("UpdateAddrModel", "accept: "+response);
                        if(response.code == Response.OK){
                            if(response.data != null){
                                addressId = response.data.addressId;
                            }
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.check(throwable);
                    }
                });
    }


    public void modifyAddr(AddrDataBeen addr, HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("addressId", addr.addressId)
                .append("address", addr.address)
                .append("communityName", addr.communityName)
                .append("detail", addr.detail)
                .append("fullAddress", addr.fullAddress)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IAddr.class)
                .modifyAddr(userId,
                        addr.addressId,
                        addr.address,
                        addr.pName,
                        addr.cityName,
                        addr.adName,
                        addr.communityName,
                        addr.detail,
                        addr.fullAddress,
                        checkSum,
                        AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d("UpdateAddrModel", "modifyAddr accept: "+response);
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.check(throwable);
                    }
                });
    }

}
