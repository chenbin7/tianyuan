package cn.tianyuan.serverlib.server;

import android.text.TextUtils;
import android.util.Log;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import java.util.UUID;

import cn.tianyuan.serverlib.server.impl.AddrImpl;
import cn.tianyuan.serverlib.server.impl.BookImpl;
import cn.tianyuan.serverlib.server.impl.PasswdImpl;
import cn.tianyuan.serverlib.server.impl.LoginImpl;
import cn.tianyuan.serverlib.server.impl.RegisterImpl;
import cn.tianyuan.serverlib.server.impl.SmsImpl;
import cn.tianyuan.serverlib.server.impl.UserImpl;

/**
 * Created by Administrator on 2017/12/5.
 */

public class SimpleServer implements HttpServerRequestCallback {
    private static final String TAG = SimpleServer.class.getSimpleName();

    private static SimpleServer mInstance;
    public static int PORT_LISTEN_DEFALT = 8887;

    AsyncHttpServer server;
    private String token;

    public synchronized static SimpleServer getInstance() {
        if (mInstance == null) {
            mInstance = new SimpleServer();
        }
        return mInstance;
    }

    private SimpleServer() {
        server = new AsyncHttpServer();
    }

    public String createToken(){
        token = UUID.randomUUID().toString();
        return token;
    }

    public void startServer() {
//        server.get("[\\d\\D]*", this);
        server.post("[\\d\\D]*", this);
        server.listen(PORT_LISTEN_DEFALT);
    }

    LoginImpl mLogin = new LoginImpl();
    RegisterImpl mRegister = new RegisterImpl();
    PasswdImpl mPwd = new PasswdImpl();
    SmsImpl mSms = new SmsImpl();
    UserImpl mUser = new UserImpl();
    AddrImpl mAddr = new AddrImpl();
    BookImpl mBook = new BookImpl();

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        String uri = request.getPath();
        Log.d(TAG, "onRequestX: uri:" + uri + "         " + request.getMethod());
        if (TextUtils.isEmpty(uri))
            return;
        if (!uri.startsWith("/logic"))
            return;
        switch (uri) {
            case "/logic/sms/getSmsCode":
                 mSms.getSmsCode(request, response);
                break;
            case "/logic/login/sms":
                mLogin.loginBySms(request, response);
                break;
            case "/logic/login/passwd":
                mLogin.loginByPasswd(request, response);
                break;
            case "/logic/register":
                mRegister.register(request, response);
                break;
            case "/logic/findback":
                mPwd.findback(request, response);
                break;
            case "/logic/pwd/modify":
                mPwd.modify(request, response);
                break;
            case "/logic/user/getInfo":
                mUser.getUserInfo(request, response);
                break;
            case "/logic/user/editHeadPic":
                mUser.updateHeader(request, response);
                break;
            case "/logic/addr/getAddressList":
                mAddr.getAddrList(request, response);
                break;
            case "/logic/addr/delAddress":
                mAddr.deleteAddr(request, response);
                break;
            case "/logic/addr/addAddress":
                mAddr.addAddr(request, response);
                break;
            case "/logic/addr/editAddress":
                mAddr.updateAddr(request, response);
                break;
            case "/logic/book/addBook":
                mBook.addBook(request, response);
                break;
            default:
                break;

        }

    }

}
