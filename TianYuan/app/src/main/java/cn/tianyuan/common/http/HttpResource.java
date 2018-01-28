package cn.tianyuan.common.http;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/8/8.
 */

public class HttpResource {
    private static final String TAG = HttpResource.class.getSimpleName();

    private static HttpResource mResource;
    public synchronized static HttpResource getInstance(){
        if(mResource == null){
            mResource = new HttpResource();
        }
        return mResource;
    }

    private Retrofit mRetrofit;

    public Retrofit getRetrofit() {
        Log.d(TAG, "getRetrofit: "+mRetrofit);
        return mRetrofit;
    }


    public void setUrl(String url) {
        Log.d(TAG, "setUrl: url:"+url);
        if (url == null || url.trim().equals(""))
            return;
        mRetrofit = getRetrofit(url);
        //取得Retrofit后，尝试连接远端服务器

    }

    private Retrofit getRetrofit(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        Log.d(TAG, "getRetrofit: retrofit = " + retrofit);
        return retrofit;
    }
}
