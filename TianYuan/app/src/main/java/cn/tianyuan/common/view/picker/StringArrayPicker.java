package cn.tianyuan.common.view.picker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.tianyuan.R;

/**
 * Created by Administrator on 2017/8/15.
 */

public class StringArrayPicker extends Picker implements View.OnClickListener {
    private static final String TAG = StringArrayPicker.class.getSimpleName();

    BasePickerView mPicker;
    List<String> mStrings;
    TextView mCancel,mFinish;
    int mPosition;

    public StringArrayPicker(@NonNull Context context) {
        super(context);
        init(context);
    }

    public StringArrayPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.picker_str_template, this, true);
        mPicker = (BasePickerView) view.findViewById(R.id.picker_str);
        mCancel = (TextView) view.findViewById(R.id.toolbar_cancel);
        mFinish = (TextView) view.findViewById(R.id.toolbar_finish);
        mCancel.setOnClickListener(this);
        mFinish.setOnClickListener(this);
        initData();
        Log.d(TAG, "init: "+mStrings);
        mPicker.setData(mStrings);
        mPicker.setOnSelectListener(new BasePickerView.onSelectListener() {
            @Override
            public void onSelectChanged(String text, int position) {
                selectData = text;
                mPosition = position;
            }
        });
    }

    public void setStrings(List<String> list){
        Log.d(TAG, "setStrings: list:"+list);
        mStrings = list;
        mPicker.setData(mStrings);
        initData();
    }

    @Override
    public void initData() {
        if(mStrings != null && mStrings.size() > 0){
            mPosition = 0;
            selectData = mStrings.get(0);
        } else {
            selectData = "";
        }
        Log.d(TAG, "showData: "+selectData+"  "+mPosition);
    }

    @Override
    public void setPickerSelectdListener(PickerSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: "+selectData+"  "+mPosition+"   "+v);
        if(v.getId() == R.id.toolbar_cancel){
            onCancel();
        } else {
            onFinish();
        }
    }

    public Object getPosition() {
        return mPosition;
    }
}
