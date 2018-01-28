package cn.tianyuan.common.view.window;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.tianyuan.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ToastWindow extends FloatWindow {

    ImageView mIcon;
    TextView mText;
    int iconRes = -1;
    String msg;

    public ToastWindow(Activity activity){
        createFloatView(activity, R.layout.wd_toast);
    }

    public ToastWindow setSrc(int iconId, String msg){
        iconRes = iconId;
        this.msg = msg;
        if(mIcon != null && mText != null) {
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(i -> {
                        mIcon.setImageResource(iconRes);
                        mText.setText(msg);
                    });
        }
        return this;
    }

    public ToastWindow setErrorToast(String text){
        setSrc(R.drawable.fail, text);
        return this;
    }

    public ToastWindow setRightToast(String text){
        setSrc(R.drawable.success, text);
        return this;
    }

    @Override
    public void onViewCreated(View view) {
        mIcon = (ImageView) view.findViewById(R.id.wd_toast_icon);
        mText = (TextView) view.findViewById(R.id.wd_toast_text);
        if(iconRes > 0){
            mIcon.setImageResource(iconRes);
        }
        if(msg != null){
            mText.setText(msg);
        }
    }
}
