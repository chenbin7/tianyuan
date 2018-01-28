package cn.tianyuan.user;

import android.text.TextUtils;
import android.util.Log;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.util.StrUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/19.
 */

public class UserPresenter {
    private static final String TAG = UserPresenter.class.getSimpleName();

    private IUserUI mUI;
    private UserModel mModel;

    public UserPresenter(IUserUI ui){
        this.mUI = ui;
        mModel = new UserModel();
    }

    public UserDataBeen getUserData(){
        return mModel.getmUserInfo();
    }

    public void pullUserData(){
        Log.d(TAG, "pullUserData: ");
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.pullUserInfo(new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            UserDataBeen data = mModel.getmUserInfo();
                            if(data == null)
                                return;
                            String account = data.telephone;
                            AppProperty.account = account;
                            AppProperty.userName = data.userName;
                            mUI.onGetAccount(account);
                            String url = StrUtils.decodeBase64(data.getUserHeadPic());
                            if(TextUtils.isEmpty(url)){
                                return;
                            } else {
                                mUI.onGetPic(url);
                            }
                        }

                        @Override
                        public void onFailed(int error, String msg) {

                        }
                    });
                });
    }


    public void setUserHeader(String headerUri) {
        mModel.setUserHeader(headerUri);
        mUI.onGetPic(headerUri);
    }
}
