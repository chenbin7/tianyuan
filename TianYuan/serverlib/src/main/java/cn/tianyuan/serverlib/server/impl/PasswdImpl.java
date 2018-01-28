package cn.tianyuan.serverlib.server.impl;

import android.text.TextUtils;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import java.util.List;
import cn.tianyuan.serverlib.db.DBManager;
import cn.tianyuan.serverlib.db.been.User;
import cn.tianyuan.serverlib.db.greendao.UserDao;
import cn.tianyuan.serverlib.server.IHttpServer;

/**
 * Created by chenbin on 2017/12/5.
 */

public class PasswdImpl extends IHttpServer {
    private static final String TAG = PasswdImpl.class.getSimpleName();

    String sms;
    String phone;
    String pwd;

    public void findback(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        sms = parms.getString("smsCode");
        phone = parms.getString("telephone");
        pwd = parms.getString("passwd");
        if(TextUtils.isEmpty(sms) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
        if(!sms.equals("1234")){
            response.send(getResponseBody(ERR_COMMON));
            return;
        }
        UserDao userDao = DBManager.getInstance()
                .getDBSession()
                .getUserDao();

        List<User> userList = null;
        try {
            userList = userDao.queryBuilder().
                    where(UserDao.Properties.Phone.eq(phone))
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(userList != null && userList.size() > 0){
            User data = userList.get(0);
            data.passwd = pwd;
            userDao.update(data);
            response.send(getResponseBody(SUCC));
        } else {
            response.send(getResponseBody(ERR_USER_NO_REG));
        }
    }

    public void modify(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        String userId = parms.getString("userId");
        String old = parms.getString("oldPasswd");
        pwd = parms.getString("passwd");
        if(TextUtils.isEmpty(userId) || TextUtils.isEmpty(old) || TextUtils.isEmpty(pwd)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
        UserDao userDao = DBManager.getInstance()
                .getDBSession()
                .getUserDao();
        List<User> userList = null;
        try {
            userList = userDao.queryBuilder().
                    where(UserDao.Properties.Id.eq(userId), UserDao.Properties.Passwd.eq(pwd))
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(userList != null && userList.size() > 0){
            User data = userList.get(0);
            data.passwd = pwd;
            userDao.update(data);
            response.send(getResponseBody(SUCC));
        } else {
            response.send(getResponseBody(ERR_COMMON));
        }
    }

}
