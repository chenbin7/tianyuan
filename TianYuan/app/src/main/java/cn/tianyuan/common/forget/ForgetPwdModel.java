package cn.tianyuan.common.forget;

import android.util.Log;

import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.common.http.smscode.ISmsCode;
import cn.tianyuan.common.http.smscode.SmsCode;
import cn.tianyuan.common.util.CheckSum;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/8/29.
 */

public class ForgetPwdModel {
    private static final String TAG = ForgetPwdModel.class.getSimpleName();

    private String number;
    private String securityCode;
    private String pwd;

    public ForgetPwdModel(){}

    public void setForgetPwdModel(String number, String securityCode, String pwd){
        this.number = number;
        this.securityCode = securityCode;
        this.pwd = pwd;
    }

    public void findBack(HttpResultListener listener){
        String checkSum = new CheckSum()
                .append("key", "value")
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IForget.class)
                .findBackPasswd(securityCode, number, pwd, checkSum)
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

    public void applySecurityCode(String phone, HttpResultListener listener){
        String checkSum = new CheckSum()
                .append("key", "value")
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(ISmsCode.class)
                .apply(SmsCode.TYPE_FORGET_PWD, phone, checkSum)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse simpleResponse) throws Exception {
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
