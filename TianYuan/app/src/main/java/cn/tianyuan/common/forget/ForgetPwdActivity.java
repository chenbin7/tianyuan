package cn.tianyuan.common.forget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.tianyuan.common.util.securitycode.SecurityCodeManager;
import cn.tianyuan.common.view.window.ToastWindow;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ForgetPwdActivity extends BaseActivity {
    private static final String TAG = ForgetPwdActivity.class.getSimpleName();

    @BindView(R.id.forget_pwd_phone)
    EditText mPhoneNum;
    @BindView(R.id.forget_pwd_pwd)
    EditText mPwd;
    @BindView(R.id.forget_pwd_security_code)
    EditText mSecurityCode;
    @BindView(R.id.forget_pwd_show_pwd)
    ImageView mPwdEye;
    @BindView(R.id.forget_pwd_apply_security_code)
    Button mSecurityBtn;
    @BindView(R.id.forget_pwd_layout_phone)
    RelativeLayout mAccountLayout;
    @BindView(R.id.forget_pwd_layout_pwd)
    RelativeLayout mPwdLayout;
    @BindView(R.id.forget_pwd_layout_security_code)
    RelativeLayout mSecurityLayout;
    @BindView(R.id.forget_pwd_commit)
    Button mBtn;
    @BindView(R.id.forget_pwd_back)
    ImageView mBack;

    ForgetPwdModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        mIsShowPwd = false;
    }
    

    @Override
    protected void onResume() {
        super.onResume();
        AnimationUtils.doRotationViews(mAccountLayout, mSecurityLayout, mPwdLayout, mSecurityBtn);
    }

    @OnClick(R.id.forget_pwd_commit)
    public void forget_pwd(){
        String number = mPhoneNum.getText().toString();
        int result = CheckUtils.checkPhoneNum(number);
        if(result != CheckUtils.PHONE_OK){
            showSnackbar("请输入正确的手机号！");
            return;
        }
        String code = mSecurityCode.getText().toString();
        result = CheckUtils.checkAuthCode(code);
        if(result != CheckUtils.AUTH_CODE_OK){
            showSnackbar("请输入正确的验证码！");
            return;
        }
        String pwd = mPwd.getText().toString();
        result = CheckUtils.checkPwd(pwd);
        if(result != CheckUtils.PWD_OK){
            showSnackbar("密码错误！");
            return;
        }
        if(mModel == null){
            mModel = new ForgetPwdModel();
        }
        mModel.setForgetPwdModel(number, code, pwd);
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.findBack(new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            Log.d(TAG, "onSucc: ");
                            new ToastWindow(ForgetPwdActivity.this)
                                    .setRightToast("密码修改成功，请登录！")
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
                                new ToastWindow(ForgetPwdActivity.this)
                                        .setErrorToast("密码修改失败！")
                                        .show(2000);
                            }
                        }
                    });
                });
    }

    @OnClick(R.id.forget_pwd_apply_security_code)
    public void applySecurityCode(){
        if(mModel == null){
            mModel = new ForgetPwdModel();
        }
        String number = mPhoneNum.getText().toString();
        int result = CheckUtils.checkPhoneNum(number);
        if(result != CheckUtils.PHONE_OK){
            showSnackbar("请输入正确的手机号！");
            return;
        }
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.applySecurityCode(number, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            Log.d(TAG, "onSucc: sms");
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            Log.d(TAG, "onFailed: sms");
                        }
                    });
                });
        SecurityCodeManager.getInstance().startSecurityCodeTimer(60, mSecurityBtn);
    }

    @OnClick(R.id.forget_pwd_back)
    public void goBack(){
        onBack();
    }

    private boolean mIsShowPwd = false;
    @OnClick(R.id.forget_pwd_show_pwd)
    public void trogglePwd(){
        mIsShowPwd = !mIsShowPwd;
        Observable.just(mIsShowPwd)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(show -> {
                    if(show){
                        mPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                        mPwdEye.setImageResource(R.drawable.login_pwd_hidden);
                    } else {
                        mPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        mPwdEye.setImageResource(R.drawable.login_pwd_show);
                    }
                });
    }

}
