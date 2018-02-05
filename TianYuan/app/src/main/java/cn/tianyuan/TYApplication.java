package cn.tianyuan;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.util.concurrent.TimeUnit;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.map.AMapLocation;
import cn.tianyuan.serverlib.LibService;
import cn.tianyuan.serverlib.db.DBManager;
import cn.tianyuan.common.util.AndroidSharedPreferences;
import io.reactivex.Observable;


/**
 * Created by chenbin on 2017/12/2.
 */

public class TYApplication extends Application {

    private static TYApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        initImageLoader();
        AndroidSharedPreferences.getInstance().init(getApplicationContext());
        AMapLocation.getInstance().initAMap(getApplicationContext());
//        HttpResource.getInstance().setUrl("http://192.168.1.5:8080");
        HttpResource.getInstance().setUrl("http://192.168.1.9:8080");
        this.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public static Activity getTopActivity() {
        return sApplication.mTopActivity;
    }

    private Activity mTopActivity;

    public static Context getAppContext(){
        return sApplication.getApplicationContext();
    }

    private void initImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .memoryCacheExtraOptions(600, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(5 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(5) //缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);//全局初始化此配置
        initImageOptions();
    }

    public static TYApplication getInstance(){
        return sApplication;
    }

    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            mTopActivity = activity;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };


    public DisplayImageOptions getOptions() {
        return options;
    }

    DisplayImageOptions options;
    private void initImageOptions(){
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.user_header) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.user_header)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.user_header)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

}
