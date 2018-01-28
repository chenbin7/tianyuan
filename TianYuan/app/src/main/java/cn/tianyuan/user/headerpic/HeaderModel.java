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
        String uri = Uri.fromFile(headerFile).toString();
        String fileUri = StrUtils.encodeBase64(uri);
        String checkSum = new CheckSum()
                .append("userId", userid)
                .getCheckSum();
        Log.w(TAG, "pushHeaderPicture: userid:"+userid+"   token:"+AppProperty.token+"  "+fileUri);
        HttpResource.getInstance()
                .getRetrofit()
                .create(IHeader.class)
                .pushHeaderPicture(userid, fileUri, checkSum, AppProperty.token)
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
