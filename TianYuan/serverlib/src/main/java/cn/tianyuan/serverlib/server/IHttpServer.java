package cn.tianyuan.serverlib.server;

import android.text.TextUtils;
import android.util.Log;

import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.tianyuan.serverlib.db.DBManager;
import cn.tianyuan.serverlib.db.been.User;
import cn.tianyuan.serverlib.db.greendao.DaoSession;
import cn.tianyuan.serverlib.db.greendao.UserDao;

/**
 * Created by chenbin on 2017/12/5.
 */

public abstract class IHttpServer {
    private static final String TAG = IHttpServer.class.getSimpleName();

    public static final int SUCC = 200;
    public static final int ERR_COMMON = 10000;
    public static final int ERR_UNKNOW = 10010;
    public static final int ERR_PARAM = 10011;
    public static final int ERR_ACCOUNT_EXIT = 10012;
    public static final int ERR_USER_NO_REG = 10013;
    public static final int ERR_LOGIN_INVALID = 10014;

    protected Multimap headers;
    protected Multimap parms;

    public DaoSession getDBSession(){
        return DBManager.getInstance().getDBSession();
    }

    public boolean checkParamsNotNull(int count, String ...parms){
        if(count <= 0)
            return false;
        if(parms == null)
            return false;
        if(parms.length != count)
            return false;
        for (int i = 0; i < parms.length; i++) {
            if(TextUtils.isEmpty(parms[i]))
                return false;
        }
        return true;
    }

    public User getUserByI(String userId){
        UserDao userDao = DBManager.getInstance()
                .getDBSession()
                .getUserDao();

        List<User> userList = null;
        try {
            userList = userDao.queryBuilder().
                    where(UserDao.Properties.Id.eq(userId))
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(userList != null && userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    public void doAnalysisRequest(AsyncHttpServerRequest request){
        headers = request.getHeaders().getMultiMap();
        parms = ((AsyncHttpRequestBody<Multimap>) request.getBody()).get();
        if (headers != null) {
            Log.d(TAG, "headers:" + headers.toString());
        }
        if (parms != null) {
            Log.d(TAG, "parms = " + parms.toString());
        }
    }

    public JSONObject getResponseBody(int code){
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("msg", "err_msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getResponseBody(int code, JSONObject data){
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("msg", "err_msg");
            if(data != null){
                json.put("data", data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getResponseBody(int code, JSONArray data){
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("msg", "err_msg");
            if(data != null){
                json.put("data", data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


}
