package cn.tianyuan.serverlib.server.impl;

import android.text.TextUtils;
import android.util.Log;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.tianyuan.serverlib.db.DBManager;
import cn.tianyuan.serverlib.db.been.User;
import cn.tianyuan.serverlib.db.greendao.UserDao;
import cn.tianyuan.serverlib.server.IHttpServer;
import cn.tianyuan.serverlib.server.SimpleServer;


/**
 * Created by chenbin on 2017/12/5.
 */

public class LoginImpl extends IHttpServer {
    private static final String TAG = LoginImpl.class.getSimpleName();

    String sms;
    String pwd;
    String phone;

    public void loginBySms(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        Log.d(TAG, "loginBySms: ");
        doAnalysisRequest(request);
        sms = parms.getString("smsCode");
        if(TextUtils.isEmpty(sms)){
            response.send(getResponseBody(ERR_COMMON));
            return;
        }
        if(sms.equals("1234")){
            response.send(getResponseBody(ERR_COMMON));
            return;
        }
        phone = parms.getString("telephone");
        User user = getUser(phone);
        if(user == null){
            response.send(getResponseBody(ERR_USER_NO_REG));
            return;
        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("id", user.getId());
                json.put("token", SimpleServer.getInstance().createToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.send(getResponseBody(SUCC, json));
        }
    }

    public void loginByPasswd(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        Log.d(TAG, "loginByPasswd: ");
        doAnalysisRequest(request);
        pwd = parms.getString("passwd");
        if(TextUtils.isEmpty(pwd)){
            response.send(getResponseBody(ERR_COMMON));
            return;
        }
        phone = parms.getString("telephone");
        User user = getUser(phone);
        if(user == null){
            response.send(getResponseBody(ERR_USER_NO_REG));
            return;
        }
        if(user.passwd.equals(pwd)) {
            JSONObject json = new JSONObject();
            try {
                json.put("id", user.getId());
                json.put("token", SimpleServer.getInstance().createToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.send(getResponseBody(SUCC, json));
        } else {
            response.send(getResponseBody(ERR_COMMON));
        }

    }

    private User getUser(String phone){
        List<User> userList = null;
        try{
            UserDao userDao = DBManager.getInstance()
                    .getDBSession()
                    .getUserDao();
            userList = userDao.queryBuilder().
                    where(UserDao.Properties.Phone.eq(phone))
                    .list();
        } catch (Exception e){
            Log.d(TAG, "getUser: Exception:"+e.toString());
        }
        Log.d(TAG, "getUser: userlist:"+userList);
        if(userList != null && userList.size() > 0){
            return userList.get(0);
        } else {
            return null;
        }
    }

}
