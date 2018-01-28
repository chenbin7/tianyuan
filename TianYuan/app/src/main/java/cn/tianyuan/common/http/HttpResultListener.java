package cn.tianyuan.common.http;


import android.app.Activity;
import android.content.Intent;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import cn.tianyuan.TYApplication;
import cn.tianyuan.common.forget.ForgetPwdActivity;
import cn.tianyuan.common.login.LoginActivity;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/9/4.
 */

public abstract class HttpResultListener {

    public static final int SUCC = 200;
    public static final int ERR_COMMON = 10000;
    public static final int ERR_UNKNOW = 10010;
    public static final int ERR_PARAM = 10011;
    public static final int ERR_ACCOUNT_EXIT = 10012;
    public static final int ERR_USER_NO_REG = 10013;
    public static final int ERR_LOGIN_INVALID = 10014;
    public static final int ERR_NO_NETWORK = -1;
    public static final int ERR_TIMEOUT = -2;
    public static final int ERR_CLIENT = -4;
    public static final int ERR_SERVER = -5;

    private static final String ERR_STR_COMMON = "服务器无响应，请稍后再试";
    private static final String ERR_STR_UNKNOW = "服务器无响应，请稍后再试";
    private static final String ERR_STR_PARAM = "数据请求异常，请稍后再试";
    private static final String ERR_STR_ACCOUNT_EXIT = "账户已存在";
    private static final String ERR_STR_USER_NO_REG = "账户未注册";
    private static final String ERR_STR_LOGIN_INVALID = "登陆失效，请重新登陆";
    private static final String ERR_STR_NO_NETWORK = "网络信号差，请确认网络连接状况后再试";
    private static final String ERR_STR_TIMEOUT = "网络连接超时，请重试";
    private static final String ERR_STR_CLIENT = "服务异常，请尝试注销后再次登陆";
    private static final String ERR_STR_SERVER = "服务器异常， 请稍后再试";

    private int errorCode;
    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public abstract void onSucc();
    public abstract void onFailed(int error, String msg);

    public boolean check(Response response){
        errorCode = response.code;
        errorMsg = response.msg;
        switch (response.code){
            case SUCC:
                onSucc();
                return true;
            case ERR_COMMON:
                errorMsg = ERR_STR_COMMON;
                break;
            case ERR_UNKNOW:
                errorMsg = ERR_STR_UNKNOW;
                break;
            case ERR_PARAM:
                errorMsg = ERR_STR_PARAM;
                break;
            case ERR_ACCOUNT_EXIT:
                errorMsg = ERR_STR_ACCOUNT_EXIT;
                break;
            case ERR_USER_NO_REG:
                if(goLoginPage()){
                    return false;
                }
                errorMsg = ERR_STR_USER_NO_REG;
                break;
            case ERR_LOGIN_INVALID:
                if(goLoginPage()){
                    return false;
                }
                errorMsg = ERR_STR_LOGIN_INVALID;
                break;
        }
        onFailed(errorCode, errorMsg);
        return false;
    }

    private boolean goLoginPage(){
        Activity top = TYApplication.getTopActivity();
        if(top instanceof LoginActivity || top instanceof ForgetPwdActivity){
            return false;
        }
        Intent intent = new Intent();
        intent.setClass(TYApplication.getAppContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        TYApplication.getAppContext().startActivity(intent);
        return true;
    }

    public void check(Throwable throwable){
        throwable.printStackTrace();
        if(throwable instanceof UnknownHostException){
            errorCode = ERR_NO_NETWORK;
            errorMsg = ERR_STR_NO_NETWORK;
        } else if(throwable instanceof TimeoutException){
            errorCode = ERR_TIMEOUT;
            errorMsg = ERR_STR_TIMEOUT;
        } else if(throwable instanceof HttpException){
            HttpException he = (HttpException) throwable;
            int httpcode = he.code();
            if(httpcode / 100 == 4){
                errorCode = ERR_CLIENT;
                errorMsg = ERR_STR_CLIENT;
            } else if(httpcode / 100 == 5){
                errorCode = ERR_SERVER;
                errorMsg = ERR_STR_SERVER;
            } else {
                errorCode = ERR_COMMON;
                errorMsg = ERR_STR_COMMON;
            }
        }
        onFailed(errorCode, errorMsg);
    }

}
