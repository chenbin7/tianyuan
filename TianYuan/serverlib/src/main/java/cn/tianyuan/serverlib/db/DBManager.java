package cn.tianyuan.serverlib.db;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.tianyuan.serverlib.db.been.Type;
import cn.tianyuan.serverlib.db.greendao.DaoMaster;
import cn.tianyuan.serverlib.db.greendao.DaoSession;
import cn.tianyuan.serverlib.db.greendao.TypeDao;
import cn.tianyuan.serverlib.server.UUIDUtil;

/**
 * Created by chenbin on 2017/12/5.
 */

public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();

    private static DBManager sDB;

    public synchronized static DBManager getInstance(){
        if(sDB == null){
            sDB = new DBManager();
        }
        return sDB;
    }

    private DBManager(){}

    public void init(Context context){
        initGreenDao(context);
    }

    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private DaoMaster mMaster;
    private DaoSession mSession;
    private void initGreenDao(Context context){
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, "tianyuan.db", null);
        mMaster = new DaoMaster(mDevOpenHelper.getWritableDb());
        mSession = mMaster.newSession();
    }

    public DaoSession getDBSession(){
        return mSession;
    }

    public void initBookType(String[] types){
        if(types != null && types.length > 0){
            return;
        }
        TypeDao typeDao = getDBSession().getTypeDao();
        for (int i = 0; i < types.length; i++) {
            Type type = new Type(UUIDUtil.getUUID(), types[i]);
            typeDao.insert(type);
        }
    }

}
