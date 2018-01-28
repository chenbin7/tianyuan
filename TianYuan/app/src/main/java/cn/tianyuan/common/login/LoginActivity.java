package cn.tianyuan.common.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.common.forget.ForgetPwdActivity;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.register.RegisterActivity;
import cn.tianyuan.common.util.AnimationUtils;
import cn.tianyuan.common.util.CheckUtils;
import cn.tianyuan.common.util.securitycode.SecurityCodeManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/28.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.login_by_account)
    TextView mLoginByAccount;
    @BindView(R.id.login_quickly)
    TextView mLoginQuickly;
    @BindView(R.id.login_phone)
    EditText mPhoneNum;
    @BindView(R.id.login_pwd)
    EditText mPwd;
    @BindView(R.id.login_security_code)
    EditText mSecurityCode;
    @BindView(R.id.login_show_pwd)
    ImageView mPwdEye;
    @BindView(R.id.login_apply_security_code)
    Button mSecurityBtn;
    @BindView(R.id.login_layout_pwd)
    RelativeLayout mPwdLayout;
    @BindView(R.id.login_layout_security_code)
    RelativeLayout mSecurityLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    public void init(){
        //显示密码框
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorSelected = getColor(R.color.color_white);
            colorUnSelected = getColor(R.color.color_text);
        } else {
            colorSelected = getResources().getColor(R.color.color_white);
            colorUnSelected = getResources().getColor(R.color.color_text);
        }
        mIsLoginByPwd = true;
        mPwdLayout.setVisibility(View.VISIBLE);
        mSecurityLayout.setVisibility(View.GONE);
        mLoginByAccount.setTextColor(colorSelected);
        mLoginQuickly.setTextColor(colorUnSelected);
        //设置密码不显示
        mIsShowPwd = false;
        mPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPwdEye.setImageResource(R.drawable.login_pwd_show);
    }

    private LoginModel mModel;
    @OnClick(R.id.login_commit)
    public void login(){
        String number = mPhoneNum.getText().toString();
        int result = CheckUtils.checkPhoneNum(number);
        if(result != CheckUtils.PHONE_OK){
            showSnackbar("请输入正确的手机号！");
            return;
        }
        String access = "";
        if(mIsLoginByPwd){
            access = mPwd.getText().toString();
            result = CheckUtils.checkPwd(access);
            if(result != CheckUtils.PWD_OK){
                showSnackbar("密码错误！");
                return;
            }
        } else {
            access = mSecurityCode.getText().toString();
            result = CheckUtils.checkAuthCode(access);
            if(result != CheckUtils.AUTH_CODE_OK){
                showSnackbar("请输入正确的验证码！");
                return;
            }
        }
        if(mModel == null){
            mModel = new LoginModel();
        }
        mModel.setLoginModel(number, access, mIsLoginByPwd);
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.login(new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            goMainActivity();
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            showSnackbar(error+"  "+msg);
                        }
                    });
                });

    }

    @OnClick(R.id.login_apply_security_code)
    public void applySecurityCode(){
        String number = mPhoneNum.getText().toString();
        int result = CheckUtils.checkPhoneNum(number);
        if(result != CheckUtils.PHONE_OK){
            showSnackbar("请输入正确的手机号！");
            return;
        }
        if(mModel == null){
            mModel = new LoginModel();
        }
        mModel.applySecurityCode(number);
        SecurityCodeManager.getInstance().startSecurityCodeTimer(6, mSecurityBtn);
    }

    int colorSelected, colorUnSelected;
    boolean mIsLoginByPwd = true;
    @OnClick(R.id.login_by_account)
    public void onLoginByAccount(){
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    AnimationUtils.doRotateAnimation(true, mSecurityLayout, mPwdLayout);
                    mLoginByAccount.setTextColor(colorSelected);
                    mLoginQuickly.setTextColor(colorUnSelected);
                });
    }

    @OnClick(R.id.login_quickly)
    public void onLoginQuickly(){
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    AnimationUtils.doRotateAnimation(true, mPwdLayout, mSecurityLayout);
                    mLoginByAccount.setTextColor(colorUnSelected);
                    mLoginQuickly.setTextColor(colorSelected);
                });
    }

    private boolean mIsShowPwd = false;
    @OnClick(R.id.login_show_pwd)
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

    @OnClick(R.id.login_register)
    public void goRegister(){
        goRegisterActivity();
    }

    @OnClick(R.id.login_forget_pwd)
    public void forgetPwd(){
        goForgetPwdActivity();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private final int requestMain = 1;
    private final int requestRegister = 2;
    private final int requestForgetPwd = 3;

    private void goRegisterActivity(){
        startActivityByCode(requestRegister, RegisterActivity.class);
    }

    private void goForgetPwdActivity(){
        startActivityByCode(requestForgetPwd, ForgetPwdActivity.class);
    }

    private void startActivityByCode(int requestCode, Class toActivity){
        Intent intent = new Intent();
        intent.setClass(this, toActivity);
        if(requestCode != requestMain){
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }
        doStartActivityForResult(intent, requestCode);
    }

}
