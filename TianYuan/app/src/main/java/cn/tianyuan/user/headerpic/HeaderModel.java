package cn.tianyuan.user.headerpic;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.Response;
import cn.tianyuan.common.util.CheckSum;
import cn.tianyuan.common.util.StrUtils;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/11/14.
 */

public class HeaderModel {
    private static final String TAG = HeaderModel.class.getSimpleName();
    private String headerUrl;

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void pushHeaderPicture(File headerFile, @NonNull HttpResultListener result){
        String userid = AppProperty.userId;

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), headerFile);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("photo", headerFile.getName(), requestFile);
        String checkSum = new CheckSum()
                .append("userId", userid)
                .getCheckSum();
        MultipartBody.Part userIdPart = MultipartBody.Part.createFormData("userId", userid);
        MultipartBody.Part checkSumPart = MultipartBody.Part.createFormData("checkSum", checkSum);
        Log.w(TAG, "pushHeaderPicture: userid:"+userid+"   token:"+AppProperty.token+"  "+headerFile.getAbsolutePath());
        HttpResource.getInstance()
                .getRetrofit()
                .create(IHeader.class)
                .pushHeaderPicture(userIdPart, partFile, checkSumPart, AppProperty.token)
                .subscribe(new Consumer<HeaderResponse>() {
                    @Override
                    public void accept(HeaderResponse response) throws Exception {
                        Log.e(TAG, "pushHeaderPicture  accept: "+response);
                        if(response.code == Response.OK){
                            if(response.data != null){
                                headerUrl = StrUtils.decodeBase64(response.data.headPicUrl);
                            }
                            result.onSucc();
                        } else {
                            result.check(response);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        result.check(throwable);
                    }
                });
    }

}
