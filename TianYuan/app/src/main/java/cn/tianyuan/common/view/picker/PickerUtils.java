package cn.tianyuan.common.view.picker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.List;

import cn.tianyuan.R;

/**
 * Created by Administrator on 2017/8/16.
 */

public class PickerUtils {
    private final static String TAG = PickerUtils.class.getSimpleName();



    public static void onStringArray(Context context, @NonNull List<String> list, @NonNull PickerResultListener listener){
        Log.d(TAG, "onStringArray: "+list);
        // 1. 新建对话框对象
        final Dialog dialog = new AlertDialog.Builder(context).create();
        // 2. 布局文件转换为View对象
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_picker_array, null);
        StringArrayPicker picker = (StringArrayPicker) layout.findViewById(R.id.array_picker);
        picker.setStrings(list);
        picker.setPickerSelectdListener(new PickerSelectedListener() {
            @Override
            public void onCancel() {
                dialog.cancel();
            }

            @Override
            public void onFinish(String result) {
                listener.onResult(result, picker.getPosition());
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setContentView(layout);
    }

    public interface PickerResultListener{
        public void onResult(String result, Object... detailedParms);
    }

}
