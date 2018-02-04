package cn.tianyuan.user.account.modifypwd;

import android.util.Log;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.user.IUser;
import cn.tianyuan.user.account.IAccount;
import cn.tianyuan.common.util.CheckSum;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/8/29.
 */

public class ModifyPwdModel {
    private static final String TAG = ModifyPwdModel.class.getSimpleName();



    public ModifyPwdModel(){}

    public void modifyPwd(String oldPwd, String newPwd, HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("oldPwd", oldPwd)
                .append("newPwd", newPwd)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IUser.class)
                .modifyPwd(userId, oldPwd, newPwd, checkSum, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse simpleResponse) throws Exception {
                        Log.d(TAG, "accept: "+simpleResponse);
                        listener.check(simpleResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.check(throwable);
                    }
                });
    }

}
