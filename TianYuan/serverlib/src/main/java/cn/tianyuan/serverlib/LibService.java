package cn.tianyuan.serverlib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.tianyuan.serverlib.db.DBManager;
import cn.tianyuan.serverlib.server.SimpleServer;

/**
 * Created by chenbin on 2017/12/5.
 */

public class LibService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.getInstance().init(getApplicationContext());
        SimpleServer.getInstance().startServer();
    }
}
