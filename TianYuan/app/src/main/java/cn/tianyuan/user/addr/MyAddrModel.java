package cn.tianyuan.user.addr;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.common.util.CheckSum;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/25.
 */

public class MyAddrModel {
    private static final String TAG = MyAddrModel.class.getSimpleName();
    private List<AddrDataBeen> addrs;

    public void addAddr(AddrDataBeen addr){
        if(addr == null){
            return;
        }
        if(addrs == null){
            addrs = new ArrayList<>();
        }
        addrs.add(addr);
    }

    public void removeAddr(int index){
        if(addrs == null)
            return;
        if(index < 0 || index > addrs.size())
            return;
        addrs.remove(index);
    }

    public List<AddrDataBeen> getAddrs() {
        return addrs;
    }


    public void deleteAddr(String addressId, HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum("userId", userId)
                .append("addressId", addressId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IAddr.class)
                .deleteAddr(userId, addressId, checkSum, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d(TAG, "deleteAddr  accept: "+response);
                        if(response.code == SimpleResponse.OK){
                            listener.onSucc();
                        } else {
                            listener.check(response);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.check(throwable);
                    }
                });
    }

    public void pullAllAddrList(@NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum("userId", userId).getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IAddr.class)
                .pullAllAddr(userId, checkSum, AppProperty.token)
                .subscribe(new Consumer<AddrResponse>() {
                    @Override
                    public void accept(AddrResponse addrResponse) throws Exception {
                        Log.d(TAG, "pullAllAddrList accept: "+addrResponse+"  "+addrResponse.data);
                        if(addrResponse.code == AddrResponse.OK){
                            if(addrResponse.data != null){
                                addrs = addrResponse.data;
                                Log.d(TAG, "accept: "+addrs.size());
                            }
                            listener.onSucc();
                        } else {
                            listener.check(addrResponse);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.check(throwable);
                    }
                });
    }
}
