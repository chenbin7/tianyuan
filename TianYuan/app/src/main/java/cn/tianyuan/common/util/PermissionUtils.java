package cn.tianyuan.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/17.
 */

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    private String[] mBasePermissions;
    private String[] mBaseDescription;
    private String[] mSecondaryPermissions;
    private Activity mActivity;
    private OnPermissionResult mListener;

    public PermissionUtils(Activity activity){
        mActivity = activity;
    }

    /**
     * 添加基本的，必须拿到的权限
     *
     * @param permissions
     * @param descriptions 与 permissions 一一对应
     */
    public void addBasePermissions(String[] permissions, String[] descriptions){
        if(permissions == null)
            return;
        mBasePermissions = permissions;
        mBaseDescription = (descriptions != null) ? descriptions : new String[0];
    }

    /**
     * 添加次要的，可有可无的权限
     * @param permissions
     */
    public void addSecondaryPermissions(String[] permissions){
        if(permissions == null)
            return;
        mSecondaryPermissions = permissions;
    }

    /**
     *开始检查、申请权限
     * @param listener
     */
    public void checkPermission(@NonNull OnPermissionResult listener){
        Log.d(TAG, "checkPermission: ");
        mListener = listener;
        //check sdk
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            listener.onGranted();
            return;
        }
        //合并权限
        String[] permissions = null;
        if(mBasePermissions != null && mBasePermissions.length > 0){
            permissions = mBasePermissions;
        }
        if(mSecondaryPermissions != null && mSecondaryPermissions.length > 0){
            if(permissions != null){
                permissions = new String[mBasePermissions.length + mSecondaryPermissions.length];
                System.arraycopy(mBasePermissions, 0, permissions, 0, mBasePermissions.length);
                System.arraycopy(mSecondaryPermissions, 0, permissions, mBasePermissions.length, mSecondaryPermissions.length);
            } else {
                permissions = mSecondaryPermissions;
            }
        }
        if(permissions == null){
            listener.onGranted();
        } else {
            //剔除已经获得的权限
            String[] applys = checkDeniedPermission(permissions);
            //此处不管用户是否已经不再提示，直接都先申请一遍。（MIUI等系统对权限做了处理）
            if(applys != null){
                applyPermission(applys);
            } else {
                listener.onGranted();
            }
        }
    }

    /**
     * 根据给出的权限，筛选出尚未获得的权限并返回
     * @param permissions
     * @return
     */
    private String[] checkDeniedPermission(String[] permissions){
        Log.d(TAG, "checkDeniedPermission: ");
        printfStrArray(permissions);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            int result = ActivityCompat.checkSelfPermission(mActivity, permissions[i]);
            Log.d(TAG, "checkDeniedPermission: "+permissions[i]+",  "+result);
            if(result != PackageManager.PERMISSION_GRANTED){
                list.add(permissions[i]);
            }
        }
        if(list.size() == 0)
            return null;
        return list.toArray(new String[0]);
    }

    /**
     * 申请权限
     * @param permissions
     */
    private void applyPermission(String[] permissions){
        Log.d(TAG, "applyPermission: ");
        printfStrArray(permissions);
        if(permissions != null && permissions.length > 0){
            ActivityCompat.requestPermissions(mActivity, permissions, 0);
        } else {
            finishPermissionCheck();
        }
    }

    /**
     * 改方法需要在权限检查类里面调用
     *
     * 检查权限申请结果
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        List<String> noPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                noPermissions.add(permissions[i]);
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: "+noPermissions.size());
        if(noPermissions.size() > 0){
            checkPermissionRefused(noPermissions.toArray(new String[0]));
        } else {
            finishPermissionCheck();
        }
    }

    /**
     * 结束权限检查，并根据权限获取结果返回
     */
    private void finishPermissionCheck(){
        if(mBasePermissions == null){
            mListener.onGranted();
            return;
        }
        if(mBasePermissions.length == 0){
            mListener.onGranted();
            return;
        }
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < mBasePermissions.length; i++) {
            if(ActivityCompat.checkSelfPermission(mActivity, mBasePermissions[i]) != PackageManager.PERMISSION_GRANTED){
                denied.add(mBasePermissions[i]);
            }
        }
        if(denied.size() == 0){
            mListener.onGranted();
        } else {
            mListener.onDiened(denied.toArray(new String[0]));
        }
    }

    /**
     * 检查被用户拒绝的权限中，有没有需要再次询问的
     * @param permissions
     */
    private void checkPermissionRefused(String[] permissions){
        Log.w(TAG, "checkPermissionRefuse: "+permissions);
        printfStrArray(permissions);
        List<String> shouldCheckAgain = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if(isBasePermission(permissions[i])){
                shouldCheckAgain.add(permissions[i]);
            }
        }
        if(shouldCheckAgain.size() > 0){
            checkPermissionsCanAskAgain(shouldCheckAgain.toArray(new String[0]));
        } else {
            finishPermissionCheck();
        }
    }

    /**
     * 检查该权限是不是基本权限
     * @param permission
     * @return
     */
    private boolean isBasePermission(String permission){
        if(mBasePermissions == null)
            return false;
        for (int i = 0; i < mBasePermissions.length; i++) {
            if(mBasePermissions[i].equals(permission)){
                return true;
            }
        }
        return false;
    }

    private String getBaseDescription(String permission){
        if(permission == null)
            return null;
        if(mBasePermissions == null)
            return null;
        for (int i = 0; i < mBasePermissions.length; i++) {
            if(mBasePermissions[i].equals(permission)){
                if(mBaseDescription != null && mBaseDescription.length > i){
                    return mBaseDescription[i];
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    Map<String, String> refuseMap;

    /**
     * 检查该权限用户是否选择不再询问
     * @param permissions
     * @return
     */
    private void checkPermissionsCanAskAgain(String[] permissions){
        Log.d(TAG, "checkPermissionsCanAskAgain: "+permissions);
        printfStrArray(permissions);
        List<String> askList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[i])){
                askList.add(permissions[i]);
            } else {
                if(refuseMap == null){
                    refuseMap = new HashMap<>();
                }
                refuseMap.put(permissions[i], "");
            }
        }
        if(askList.size() > 0){
            showDescriptionsDialog(askList.toArray(new String[0]));
        } else {
            finishPermissionCheck();
        }
    }

    /**
     * 弹出请求再次申请的dialog
     * @param permissions
     */
    private void showDescriptionsDialog(String[] permissions) {
        if(permissions == null){
            finishPermissionCheck();
            return;
        }
        if(permissions.length == 0){
            finishPermissionCheck();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < permissions.length; i++) {
            if(isBasePermission(permissions[i])){
                String reason = getBaseDescription(permissions[i]);
                if(reason != null){
                    sb.append(reason).append("\n");
                }
            }
        }
        String msg = sb.toString();
        if(TextUtils.isEmpty(msg)){
            finishPermissionCheck();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishPermissionCheck();
            }
        });
        builder.setPositiveButton("再次申请", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                applyPermission(permissions);
            }
        });
        builder.show();
    }

    public interface OnPermissionResult{
        public void onGranted();
        public void onDiened(String[] dinied);
    }

    private void printfStrArray(String[] arr){
        if(arr == null)
            return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]).append(",  ");
        }
        Log.e(TAG, "printfStrArray: "+sb.toString());
    }

}
