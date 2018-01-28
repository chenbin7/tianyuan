package cn.tianyuan.common.view.picker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/8/15.
 */

public abstract class Picker extends FrameLayout {
    protected PickerSelectedListener mListener;
    String selectData;

    public Picker(@NonNull Context context) {
        super(context);
    }

    public Picker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void initData();

    public void setPickerSelectdListener(PickerSelectedListener listener) {
        this.mListener = listener;
    }

    public void onCancel(){
        if(mListener != null){
            mListener.onCancel();
        }
    }

    public void onFinish(){
        if(mListener != null){
            mListener.onFinish(selectData);
        }
    }


}
