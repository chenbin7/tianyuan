package cn.tianyuan.common.view.window;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.tianyuan.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ToastCheckIconWindow extends FloatWindow {

    ImageView mClose, mIcon;
    Button mCommit;
    TextView mTitle, mContent;
    String title, postiveBtn, content;
    int iconId = -1;
    View.OnClickListener listenerPositive, listenerNegative;
    boolean disClose = false;

    public ToastCheckIconWindow(Activity activity){
        createFloatView(activity, R.layout.wd_toast_check_icon);
    }

    public ToastCheckIconWindow setIcon(int resId){
        this.iconId = resId;
        setIconDraw(resId);
        return this;
    }

    public ToastCheckIconWindow setTitle(String title){
        this.title = title;
        setText(mTitle, title);
        return this;
    }

    public ToastCheckIconWindow setContent(String content){
        this.content = content;
        setText(mContent, title);
        return this;
    }

    public ToastCheckIconWindow setPositive(String msg, View.OnClickListener listener){
        this.listenerPositive = listener;
        this.postiveBtn = msg;
        setText(mCommit, msg);
        return this;
    }

    public ToastCheckIconWindow setNegative(View.OnClickListener listener){
        this.listenerNegative = listener;
        return this;
    }

    public ToastCheckIconWindow disableClose(){
        disClose = true;
        if(mClose != null){
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(i -> {
                        mClose.setVisibility(View.GONE);
                    });
        }
        return this;
    }

    private void setText(TextView view, String text){
        if(view == null)
            return;
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    view.setText(text);
                });
    }

    private void setIconDraw(int resId){
        if(mIcon == null)
            return;
        Observable.just(resId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                    mIcon.setImageResource(id);
                });
    }


    @Override
    public void onViewCreated(View view) {
        mClose = (ImageView) view.findViewById(R.id.close);
        mIcon = (ImageView) view.findViewById(R.id.toast_check_icon);
        mTitle = (TextView) view.findViewById(R.id.toast_check_title);
        mContent = (TextView) view.findViewById(R.id.toast_check_context);
        mCommit = (Button) view.findViewById(R.id.toast_check_positive);
        mCommit.setOnClickListener(listener);
        mClose.setOnClickListener(listener);
        mTitle.setText(title);
        mContent.setText(content);
        mCommit.setText(postiveBtn);
        if(iconId > 0){
            mIcon.setImageResource(iconId);
        }
        if(disClose){
            mClose.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeWindow();
            switch (v.getId()){
                case R.id.close:
                    if(listenerNegative != null){
                        listenerNegative.onClick(v);
                    }
                    return;
                case R.id.toast_check_positive:
                    if(listenerPositive != null){
                        listenerPositive.onClick(v);
                    }
                    return;
                default:
                    break;
            }
        }
    };
}
