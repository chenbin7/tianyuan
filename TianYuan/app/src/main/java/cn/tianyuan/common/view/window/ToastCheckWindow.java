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

public class ToastCheckWindow extends FloatWindow {

    ImageView mClose;
    Button mCommit;
    TextView mTitle, mContent;
    String title, postiveBtn, content;
    View.OnClickListener listenerPositive, listenerNegative;
    boolean disClose = false;

    public ToastCheckWindow(Activity activity){
        createFloatView(activity, R.layout.wd_toast_check);
    }

    public ToastCheckWindow setTitle(String title){
        this.title = title;
        setText(mTitle, title);
        return this;
    }

    public ToastCheckWindow setContent(String content){
        this.content = content;
        setText(mContent, title);
        return this;
    }

    public ToastCheckWindow setPositive(String msg, View.OnClickListener listener){
        this.listenerPositive = listener;
        this.postiveBtn = msg;
        setText(mCommit, msg);
        return this;
    }

    public ToastCheckWindow setNegative(View.OnClickListener listener){
        this.listenerNegative = listener;
        return this;
    }

    public ToastCheckWindow disableClose(){
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


    @Override
    public void onViewCreated(View view) {
        mClose = (ImageView) view.findViewById(R.id.close);
        mTitle = (TextView) view.findViewById(R.id.toast_check_title);
        mContent = (TextView) view.findViewById(R.id.toast_check_context);
        mCommit = (Button) view.findViewById(R.id.toast_check_positive);
        mCommit.setOnClickListener(listener);
        mClose.setOnClickListener(listener);
        mTitle.setText(title);
        mContent.setText(content);
        mCommit.setText(postiveBtn);
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
