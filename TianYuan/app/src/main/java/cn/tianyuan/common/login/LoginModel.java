package cn.tianyuan.common.login;

import android.util.Log;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.smscode.SmsCode;
import cn.tianyuan.common.util.CheckSum;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/29.
 */

public class LoginModel {
    private static final String TAG = LoginModel.class.getSimpleName();
    private String phoneNum;
    private String access;
    private boolean byPwd;

    public LoginModel() {
    }
    public void setLoginModel(String phoneNum, String access, boolean byPwd) {
        this.phoneNum = phoneNum;
        this.access = access;
        this.byPwd = byPwd;
    }

    public void login(HttpResultListener listener){
        String checksum = new CheckSum()
                .append("key", "value")
                .getCheckSum();
        if(byPwd){
            HttpResource.getInstance()
                    .getRetrofit()
                    .create(ILogin.class)
                    .loginByPasswd(phoneNum, access, checksum)
                    .subscribe(new Consumer<LoginResponse>() {
                        @Override
                        public void accept(LoginResponse response) throws Exception {
                            Log.d(TAG, "accept: byPasswd:"+response);
                            if(response.code == HttpResultListener.SUCC && response.data != null){
                                AppProperty.userId = response.data.id;
                                AppProperty.token = response.data.token;
                            }
                            listener.check(response);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            listener.check(throwable);
                        }
                    });
        } else {
            HttpResource.getInstance()
                    .getRetrofit()
                    .create(ILogin.class)
                    .loginBySms(phoneNum, access, checksum)
                    .subscribe(new Consumer<LoginResponse>() {
                        @Override
                        public void accept(LoginResponse response) throws Exception {
                            Log.d(TAG, "accept: bySmsL:"+response);
                            if(response.code == HttpResultListener.SUCC && response.data != null){
                                AppProperty.userId = response.data.id;
                                AppProperty.token = response.data.token;
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
    }

    public void applySecurityCode(String number){
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    new SmsCode().apply(number, SmsCode.TYPE_REGISTER, null);
                });

    }

}
