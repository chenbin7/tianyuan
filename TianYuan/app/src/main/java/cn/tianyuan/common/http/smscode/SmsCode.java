package cn.tianyuan.common.http.smscode;

import android.util.Log;

import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.common.util.CheckSum;
import cn.tianyuan.common.util.StrUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/10.
 */

public class SmsCode {
    private static final String TAG = SmsCode.class.getSimpleName();

    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_LOGIN_QUICKLY = 2;
    public static final int TYPE_FORGET_PWD = 3;

    public SmsCode(){}

    public boolean apply(String phoneNumber, int type, HttpResultListener listener){
        if(phoneNumber == null){
            return false;
        }
        if(!StrUtils.isMobileNO(phoneNumber)){
            return false;
        }
        Log.d(TAG, "apply: "+phoneNumber+"  type:"+type);
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    doApply(type, phoneNumber, listener);
                });
        return true;
    }

    private void doApply(int type, String phone, HttpResultListener listener){
        if(true){
            if(listener != null){
                listener.onSucc();
            }
            return;
        }
        String checksum = new CheckSum()
                .append("type", type)
                .append("telephone", phone)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(ISmsCode.class)
                .apply(type, phone, checksum)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse code) throws Exception {
                        Log.i(TAG, "accept: "+code.code+"  "+code.msg);
                        if(listener != null) {
                            listener.check(code);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(listener != null) {
                            listener.check(throwable);
                        } else {
                            throwable.printStackTrace();
                        }
                    }
                });
    }


    public boolean auth(String phoneNumber, String smsCode, HttpResultListener listener){
        if(phoneNumber == null || smsCode == null){
            return false;
        }
        if(!StrUtils.isMobileNO(phoneNumber)){
            return false;
        }
        if(!StrUtils.isAuthCode(smsCode))
        Log.d(TAG, "auth: "+phoneNumber+"  sms:"+smsCode);
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    doAuth(smsCode, "+86"+phoneNumber, listener);
                });
        return true;
    }

    private void doAuth(String sms, String phone, HttpResultListener listener){
        String checksum = new CheckSum()
                .append("telephone", phone)
                .append("smsCode", sms)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(ISmsCode.class)
                .auth(phone, sms, checksum)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<AuthResponse>() {
                    @Override
                    public void accept(AuthResponse code) throws Exception {
                        Log.i(TAG, "accept: "+code.code+"  "+code.msg);
                        if(listener != null) {
                            if (code.code == 200) {
                                listener.onSucc();
                            } else {
                                listener.check(code);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(listener != null) {
                            listener.check(throwable);
                        } else {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

}
