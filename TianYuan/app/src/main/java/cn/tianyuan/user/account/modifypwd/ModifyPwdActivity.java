package cn.tianyuan.user.account.modifypwd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.util.AnimationUtils;
import cn.tianyuan.common.util.CheckUtils;
import cn.tianyuan.common.view.window.ToastWindow;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ModifyPwdActivity extends BaseActivity {
    private static final String TAG = ModifyPwdActivity.class.getSimpleName();

    @BindView(R.id.modify_pwd_old)
    EditText mPwdOld;
    @BindView(R.id.modify_pwd_new)
    EditText mPwd;
    @BindView(R.id.modify_pwd_again)
    EditText mPwdAgain;

    @BindView(R.id.modify_pwd_layout_phone)
    RelativeLayout mAccountLayout;
    @BindView(R.id.modify_pwd_layout_pwd)
    RelativeLayout mPwdLayout;
    @BindView(R.id.modify_pwd_layout_security_code)
    RelativeLayout mSecurityLayout;
    @BindView(R.id.modify_pwd_commit)
    Button mBtn;

    ModifyPwdModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
    }
    

    @Override
    protected void onResume() {
        super.onResume();
        AnimationUtils.doRotationViews(mAccountLayout, mSecurityLayout, mPwdLayout);
    }

    @OnClick(R.id.modify_pwd_commit)
    public void forget_pwd(){
        String oldpwd = mPwdOld.getText().toString();
        int result = CheckUtils.checkPwd(oldpwd);
        if(result != CheckUtils.PHONE_OK){
            showSnackbar("请输入正确的原密码！");
            return;
        }
        String pwd = mPwd.getText().toString();
        result = CheckUtils.checkPwd(pwd);
        if(result != CheckUtils.PWD_OK){
            showSnackbar("请输入包括大小写几数字在内的8-16位密码！");
            return;
        }
        String again = mPwdAgain.getText().toString();
        if(!pwd.equals(again)){
            showSnackbar("请输入相同的新密码！");
            return;
        }

        if(mModel == null){
            mModel = new ModifyPwdModel();
        }
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.modifyPwd(oldpwd, pwd, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            Log.d(TAG, "onSucc: ");
                            new ToastWindow(ModifyPwdActivity.this)
                                    .setRightToast("密码修改成功！")
                                    .show(2000);
                            Observable.just(0)
                                    .delay(2000, TimeUnit.MILLISECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(i -> {
                                        doFinish(RESULT_OK);
                                    });
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            Log.d(TAG, "onFailed: "+error);
                            if(error == HttpResultListener.ERR_USER_NO_REG){
                                Observable.just(0)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(i -> {
                                            showSnackbar("账号未注册，请先注册！");
                                        });
                            } else {
                                new ToastWindow(ModifyPwdActivity.this)
                                        .setErrorToast("密码修改失败！")
                                        .show(2000);
                            }
                        }
                    });
                });
    }


    @OnClick(R.id.modify_pwd_back)
    public void goBack(){
        onBack();
    }

}
