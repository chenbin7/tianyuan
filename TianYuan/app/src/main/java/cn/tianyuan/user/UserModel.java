package cn.tianyuan.user;

import android.util.Log;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.common.util.CheckSum;
import cn.tianyuan.common.util.StrUtils;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/10/19.
 */

public class UserModel {
    private static final String TAG = UserModel.class.getSimpleName();
    private UserDataBeen mUserInfo;

    public UserDataBeen getmUserInfo() {
        return mUserInfo;
    }

    public void pullUserInfo(HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IUser.class)
                .pullUserInfo(userId, checkSum, AppProperty.token)
                .subscribe(new Consumer<UserInfoResponse>() {
                    @Override
                    public void accept(UserInfoResponse userInfo) throws Exception {
                        Log.d(TAG, "accept: "+userInfo+"  "+userInfo.data);
                        if(userInfo.code == UserInfoResponse.OK){
                            mUserInfo = userInfo.data;
                            listener.onSucc();
                        } else {
                            listener.check(userInfo);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.check(throwable);
                    }
                });
    }

    public void modifyPwd(String oldPasswd, String newPasswd, HttpResultListener listener){
        String oldpwd = StrUtils.getMD5_Safe(oldPasswd);
        String newpwd = StrUtils.getMD5_Safe(newPasswd);
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("oldPasswd", oldpwd)
                .append("newPasswd", newpwd)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IUser.class)
                .modifyPwd(userId,oldpwd, newpwd, checkSum, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse modifyResponse) throws Exception {
                        Log.d(TAG, "modifyPwd accept: "+modifyResponse);
                        if(modifyResponse.code == SimpleResponse.OK){
                            listener.onSucc();
                        } else {
                            listener.check(modifyResponse);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.check(throwable);
                    }
                });
    }

    public void modifyInfo(String name, String phone, String sex, HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("name", name)
                .append("phone", phone)
                .append("sex", sex)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IUser.class)
                .modifyInfo(userId,name, phone,sex, checkSum, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse modifyResponse) throws Exception {
                        Log.d(TAG, "modifyPwd accept: "+modifyResponse);
                        if(modifyResponse.code == SimpleResponse.OK){
                            listener.onSucc();
                        } else {
                            listener.check(modifyResponse);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.check(throwable);
                    }
                });
    }

    public void setUserHeader(String headerUri) {
        if(mUserInfo != null){
            mUserInfo.userHeadPic = headerUri;
        }
    }
}
