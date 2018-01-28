package cn.tianyuan.serverlib.server.impl;

import android.util.Log;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.util.List;

import cn.tianyuan.serverlib.db.DBManager;
import cn.tianyuan.serverlib.db.been.User;
import cn.tianyuan.serverlib.db.greendao.UserDao;
import cn.tianyuan.serverlib.server.IHttpServer;
import cn.tianyuan.serverlib.server.UUIDUtil;

/**
 * Created by chenbin on 2017/12/5.
 */

public class RegisterImpl extends IHttpServer {
    private static final String TAG = RegisterImpl.class.getSimpleName();
    
    String sms;
    String phone;
    String pwd;

    public void register(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        Log.d(TAG, "register: ");
        doAnalysisRequest(request);
        sms = parms.getString("smsCode");
        phone = parms.getString("telephone");
        pwd = parms.getString("passwd");
        UserDao user = DBManager.getInstance()
                .getDBSession()
                .getUserDao();
        List<User> userList = null;
        try{
            userList = user.queryBuilder().
                    where(UserDao.Properties.Phone.eq(phone))
                    .list();
        } catch (Exception e){
            Log.d(TAG, "register: Exception:"+e.toString());
        }
        Log.d(TAG, "register: userlist:"+userList);
        if(userList != null && userList.size() > 0){
            response.send(getResponseBody(ERR_ACCOUNT_EXIT));
        } else {
            User data = new User(UUIDUtil.getUUID(), "name", phone, pwd, "none");
            long result = user.insert(data);
            Log.d(TAG, "register: result = "+result);
            if(result > 0) {
                response.send(getResponseBody(SUCC));
            } else {
                response.send(getResponseBody(ERR_COMMON));
            }
        }
    }
}
