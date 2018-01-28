package cn.tianyuan.serverlib.server.impl;

import android.util.Log;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import cn.tianyuan.serverlib.server.IHttpServer;

/**
 * Created by chenbin on 2017/12/5.
 */

public class SmsImpl extends IHttpServer {
    private static final String TAG = IHttpServer.class.getSimpleName();

    String type;
    String phone;

    public void getSmsCode(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        type = parms.getString("type");
        phone = parms.getString("telephone");
        Log.d(TAG, "onRequest: "+type+"  "+phone);
        response.send(getResponseBody(SUCC));
    }
}
