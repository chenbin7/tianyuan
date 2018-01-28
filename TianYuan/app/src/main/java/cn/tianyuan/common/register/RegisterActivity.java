package cn.tianyuan.common.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
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

/**
 * Created by Administrator on 2017/8/28.
 */

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();


    @BindView(R.id.register_phone)
    EditText mPhoneNum;
    @BindView(R.id.register_pwd)
    EditText mPwd;
    @BindView(R.id.register_security_code)
    EditText mSecurityCode;
    @BindView(R.id.register_show_pwd)
    ImageView mPwdEye;
    @BindView(R.id.register_apply_security_code)
    Button mSecurityBtn;
    @BindView(R.id.register_layout_phone)
    RelativeLayout mPhoneLayout;
    @BindView(R.id.register_layout_pwd)
    RelativeLayout mPwdLayout;
    @BindView(R.id.register_layout_security_code)
    RelativeLayout mSecurityLayout;
    @BindView(R.id.register_commit)
    Button mBtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mIsShowPwd = false;
    }



    RegisterModel mModel;

    @Override
    protected void onResume() {
        super.onResume();
        AnimationUtils.doRotationViews(mPhoneLayout, mSecurityLayout, mPwdLayout, mSecurityBtn);
    }

    @OnClick(R.id.register_commit)
    public void register(){
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
            mModel = new RegisterModel();
        }
        mModel.register(number, code, pwd, new HttpResultListener() {
            @Override
            public void onSucc() {
                Log.d(TAG, "onSucc: ");
                new ToastWindow(RegisterActivity.this)
                        .setRightToast("注册成功")
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
                if(error == HttpResultListener.ERR_ACCOUNT_EXIT) {
                    Observable.just(0)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(i -> {
                                showSnackbar("改账户已经被注册！");
                            });
                } else {
                    new ToastWindow(RegisterActivity.this)
                            .setErrorToast("注册失败")
                            .show(2000);
                }
            }
        });
    }

    @OnClick(R.id.register_apply_security_code)
    public void applySecurityCode(){
        String number = mPhoneNum.getText().toString();
        int result = CheckUtils.checkPhoneNum(number);
        if(result != CheckUtils.PHONE_OK){
            showSnackbar("请输入正确的手机号！");
            return;
        }
        if(mModel == null){
            mModel = new RegisterModel();
        }
        mModel.applySecurityCode(number);
        SecurityCodeManager.getInstance().startSecurityCodeTimer(60, mSecurityBtn);
    }

    private boolean mIsShowPwd = false;
    @OnClick(R.id.register_show_pwd)
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

    private void onIgnore(){
        //TODO
        goUserActivity();
    }

    private void onGoAuthRealName(){
        Intent intent = new Intent();
//        intent.setClass(this, AuthActivity.class);
        doStartActivity(intent);
    }

    private void goUserActivity(){
        Intent intent = new Intent();
//        intent.setClass(this, UserActivity.class);
        doStartActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            doFinish(RESULT_CANCELED);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
