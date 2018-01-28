package cn.tianyuan.common.register;

import android.util.Log;

import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.common.http.smscode.SmsCode;
import cn.tianyuan.common.util.CheckSum;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/29.
 */

public class RegisterModel {
    private static final String TAG = RegisterModel.class.getSimpleName();

    private String number;
    private String securityCode;
    private String pwd;

    public RegisterModel(){}


    public void register(String number, String securityCode, String pwd,HttpResultListener listener){
        this.number = number;
        this.securityCode = securityCode;
        this.pwd = pwd;
        String checkSum = new CheckSum()
                .append("smsCode", this.securityCode)
                .append("smsCode", this.number)
                .append("passwd", this.pwd)
                .getCheckSum();
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    doRegister(checkSum, listener);
                });
    }

    private void doRegister(String checkSum, HttpResultListener listener){
        HttpResource.getInstance()
                .getRetrofit()
                .create(IRegister.class)
                .register(securityCode, number, pwd, checkSum)
                .subscribe(new Consumer<SimpleResponse>() {
                               @Override
                               public void accept(SimpleResponse code) throws Exception {
                                   Log.d(TAG, "accept: "+code);
                                   if(listener != null){
                                      listener.check(code);
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   if(listener != null){
                                       listener.check(throwable);
                                   }
                               }
                           }
                );
    }


    public void applySecurityCode(String phoneNum){
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    new SmsCode().apply(phoneNum, SmsCode.TYPE_LOGIN_QUICKLY, null);
                });
    }

}
