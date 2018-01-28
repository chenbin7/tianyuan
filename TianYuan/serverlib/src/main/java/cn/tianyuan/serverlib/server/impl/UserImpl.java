package cn.tianyuan.serverlib.server.impl;

import android.text.TextUtils;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.tianyuan.serverlib.db.DBManager;
import cn.tianyuan.serverlib.db.been.User;
import cn.tianyuan.serverlib.db.greendao.UserDao;
import cn.tianyuan.serverlib.server.IHttpServer;

/**
 * Created by chenbin on 2017/12/5.
 */

public class UserImpl extends IHttpServer {
    private static final String TAG = UserImpl.class.getSimpleName();

    String id;

    public void getUserInfo(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        id = parms.getString("userId");
        if(TextUtils.isEmpty(id)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
        UserDao userDao = DBManager.getInstance()
                .getDBSession()
                .getUserDao();

        List<User> userList = null;
        try {
            userList = userDao.queryBuilder().
                    where(UserDao.Properties.Id.eq(id))
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(userList != null && userList.size() > 0){
            User user = userList.get(0);
            JSONObject json = new JSONObject();
            try {
                json.put("userId", user.getId());
                json.put("userName", user.getName());
                json.put("userHeadPic", user.getHeader());
                json.put("telephone", user.getPhone());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.send(getResponseBody(SUCC, json));
        } else {
            response.send(getResponseBody(ERR_USER_NO_REG));
        }
    }

    public void updateHeader(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        id = parms.getString("userId");
        String uri = parms.getString("fileUri");
        if(TextUtils.isEmpty(id) || TextUtils.isEmpty(uri)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
        UserDao userDao = DBManager.getInstance()
                .getDBSession()
                .getUserDao();

        List<User> userList = null;
        try {
            userList = userDao.queryBuilder().
                    where(UserDao.Properties.Id.eq(id))
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(userList != null && userList.size() > 0){
            User user = userList.get(0);
            user.setHeader(uri);
            userDao.update(user);
            JSONObject json = new JSONObject();
            try {
                json.put("headPicUrl", uri);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.send(getResponseBody(SUCC, json));
        } else {
            response.send(getResponseBody(ERR_USER_NO_REG));
        }
    }
}
