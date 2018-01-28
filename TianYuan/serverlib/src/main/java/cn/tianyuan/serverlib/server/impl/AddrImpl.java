package cn.tianyuan.serverlib.server.impl;

import android.text.TextUtils;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.tianyuan.serverlib.db.DBManager;
import cn.tianyuan.serverlib.db.been.Addr;
import cn.tianyuan.serverlib.db.been.User;
import cn.tianyuan.serverlib.db.greendao.AddrDao;
import cn.tianyuan.serverlib.db.greendao.UserDao;
import cn.tianyuan.serverlib.server.IHttpServer;
import cn.tianyuan.serverlib.server.UUIDUtil;

/**
 * Created by chenbin on 2017/12/5.
 */

public class AddrImpl extends IHttpServer {
    private static final String TAG = AddrImpl.class.getSimpleName();

    public void getAddrList(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        String userid = parms.getString("userId");
        if(TextUtils.isEmpty(userid)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
        AddrDao addrDao = DBManager.getInstance()
                .getDBSession()
                .getAddrDao();

        List<Addr> addrList = null;
        try {
            addrList = addrDao.queryBuilder().
                    where(AddrDao.Properties.UserId.eq(userid))
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(addrList != null && addrList.size() > 0){
            JSONArray ja = new JSONArray();
            try {
                for (int i = 0; i < addrList.size(); i++) {
                    Addr addr = addrList.get(i);
                    JSONObject json = new JSONObject();
                    json.put("addressId", addr.getId());
                    json.put("address", addr.getAddres());
                    json.put("communityName", addr.getCommunityName());
                    json.put("detail", addr.getAddrDetail());
                    json.put("fullAddress", addr.getFullAddr());
                    ja.put(json);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.send(getResponseBody(SUCC, ja));
        } else {
            response.send(getResponseBody(SUCC));
        }
    }

    public void updateAddr(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        String addressId = parms.getString("addressId");
        String address = parms.getString("address");
        String communityName = parms.getString("communityName");
        String detail = parms.getString("detail");
        String fullAddress = parms.getString("fullAddress");
        if(TextUtils.isEmpty(fullAddress) || TextUtils.isEmpty(addressId)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
        AddrDao addrDao = DBManager.getInstance()
                .getDBSession()
                .getAddrDao();

        List<Addr> addrList = null;
        try {
            addrList = addrDao.queryBuilder().
                    where(AddrDao.Properties.Id.eq(addressId))
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(addrList != null && addrList.size() > 0){
            Addr addr = addrList.get(0);
            addr.setAddrDetail(detail);
            addr.setAddres(address);
            addr.setCommunityName(communityName);
            addr.setFullAddr(fullAddress);
            addrDao.update(addr);
            response.send(getResponseBody(SUCC));
        } else {
            response.send(getResponseBody(ERR_COMMON));
        }
    }

    public void addAddr(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        String userId = parms.getString("userId");
        String addressId = UUIDUtil.getUUID();
        String address = parms.getString("address");
        String communityName = parms.getString("communityName");
        String detail = parms.getString("detail");
        String fullAddress = parms.getString("fullAddress");
        if(TextUtils.isEmpty(userId) || TextUtils.isEmpty(fullAddress)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
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
        if(userList != null && userList.size() > 0){
            AddrDao addrDao = DBManager.getInstance()
                    .getDBSession()
                    .getAddrDao();
            Addr addr = new Addr(addressId, userId, address, communityName, detail, fullAddress);
            long result = addrDao.insert(addr);
            if(result > 0) {
                response.send(getResponseBody(SUCC));
            } else {
                response.send(getResponseBody(ERR_COMMON));
            }
        } else {
            response.send(getResponseBody(ERR_UNKNOW));
        }
    }

    public void deleteAddr(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        String addressId = parms.getString("addressId");
        if(TextUtils.isEmpty(addressId)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
        AddrDao addrDao = DBManager.getInstance()
                .getDBSession()
                .getAddrDao();
        List<Addr> addrList = null;
        try {
            addrList = addrDao.queryBuilder().
                    where(AddrDao.Properties.Id.eq(addressId))
                    .list();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(addrList != null && addrList.size() > 0){
            Addr addr = addrList.get(0);
            addrDao.delete(addr);
            response.send(getResponseBody(SUCC));
        } else {
            response.send(getResponseBody(ERR_COMMON));
        }
    }

}
