package cn.tianyuan.common.view.window;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/9/26.
 */

public abstract class FloatWindow {
    private static final String TAG = FloatWindow.class.getSimpleName();

    //定义浮动窗口布局
    private ViewGroup mFloatLayout;
    private ViewGroup mActivityLayout;
    private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象

    protected Activity activity;
    private int resId = 0;

    public abstract void onViewCreated(View view);

    public void closeWindow() {
        Log.d(TAG, "closeWindow: "+this);
        if (mFloatLayout != null) {
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(i -> {
                        mActivityLayout.removeView(mFloatLayout);
                        mFloatLayout = null;
                    });
        }
    }

    public FloatWindow createFloatView(Activity activity, int resId) {
        if (activity == null || resId <= 0)
            return null;
        mActivityLayout = (ViewGroup) activity.getWindow().getDecorView();
        this.activity = activity;
        this.resId = resId;
        return this;
    }

    public void show(int duration) {
        show();
        Observable.just(0)
                .delay(duration, TimeUnit.MILLISECONDS)
                .subscribe(i -> {
                    closeWindow();
                });
    }

    public void show() {
        if (mFloatLayout != null) {
            return;
        }
        if (activity == null || resId == 0) {
            return;
        }

        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    initWindowManager();
                    addWindowToWM(activity);
                    mFloatLayout.setFocusable(true);
                    mFloatLayout.setFocusableInTouchMode(true);
                    boolean focus = mFloatLayout.requestFocus();
                    mFloatLayout.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if(keyCode == KeyEvent.KEYCODE_BACK){
                                if(event.getAction() == KeyEvent.ACTION_UP) {
                                    closeWindow();
                                }
                                return true;
                            }
                            return false;
                        }
                    });
                });
    }

    private void initWindowManager() {
        wmParams = new WindowManager.LayoutParams();
        //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
//        mWindowManager = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    private void addWindowToWM(Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        //获取浮动窗口视图所在布局
        mFloatLayout = (ViewGroup) inflater.inflate(resId, null);
        onViewCreated(mFloatLayout);
        //添加mFloatLayout
        mActivityLayout.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.EXACTLY), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.EXACTLY));
    }
}
