package cn.tianyuan.user.account;

import android.util.Log;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.common.util.CheckSum;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/24.
 */

public class AccountModel {
    private static final String TAG = AccountModel.class.getSimpleName();

    public void logout(){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IAccount.class)
                .logout(userId, checkSum, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse logoutResponse) throws Exception {
                        Log.d(TAG, "logout  accept: "+logoutResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

}
