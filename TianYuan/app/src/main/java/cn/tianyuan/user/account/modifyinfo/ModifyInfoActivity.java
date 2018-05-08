package cn.tianyuan.user.account.modifyinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import cn.tianyuan.user.UserDataBeen;
import cn.tianyuan.user.UserModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ModifyInfoActivity extends BaseActivity {
    private static final String TAG = ModifyInfoActivity.class.getSimpleName();

    @BindView(R.id.info_name)
    EditText mName;
    @BindView(R.id.info_phone)
    EditText mPhone;
    @BindView(R.id.sex_radio)
    RadioGroup mSexRadio;

    @BindView(R.id.modify_info_name)
    RelativeLayout mNameLayout;
    @BindView(R.id.modify_info_phone)
    RelativeLayout mPhoneLayout;
    @BindView(R.id.modify_info_sex)
    RelativeLayout mSexLayout;
    @BindView(R.id.commit)
    Button mBtn;

    UserDataBeen mUserData;
    UserModel mModel;
    String sex = "男";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        ButterKnife.bind(this);
        mUserData = getIntent().getParcelableExtra("userdata");
        if(mUserData.sex != null) {
            if (mUserData.sex.trim().equals("w")) {
                mSexRadio.check(R.id.woman);
            }
        }
        mName.setText(mUserData.userName);
        mPhone.setText(mUserData.telephone);
        mSexRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.man){
                    sex = "m";
                } else {
                    sex = "w";
                }
            }
        });
    }
    

    @Override
    protected void onResume() {
        super.onResume();
        AnimationUtils.doRotationViews(mNameLayout, mSexLayout, mPhoneLayout);
    }

    @OnClick(R.id.commit)
    public void updateInfo(){
        String name = mName.getText().toString();
        if(TextUtils.isEmpty(name)){
            showSnackbar("请输入名字！");
            return;
        }
        String phone= mPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            showSnackbar("请输入电话号码！");
            return;
        }
        if(CheckUtils.checkPhoneNum(phone) != CheckUtils.PHONE_OK){
            showSnackbar("请输入正确的电话号码！");
            return;
        }
        if(mModel == null){
            mModel = new UserModel();
        }
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.modifyInfo(mName.getText().toString(), mPhone.getText().toString(), sex, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            Log.d(TAG, "onSucc: ");
                            new ToastWindow(ModifyInfoActivity.this)
                                    .setRightToast("用户信息修改成功！")
                                    .show(2000);
                            Observable.just(0)
                                    .delay(2000, TimeUnit.MILLISECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(i -> {
                                        Intent intent = new Intent();
                                        intent.putExtra("name",  mName.getText().toString());
                                        intent.putExtra("phone",  mPhone.getText().toString());
                                        intent.putExtra("sex",  sex);
                                        doFinish(RESULT_OK, intent);
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
                                new ToastWindow(ModifyInfoActivity.this)
                                        .setErrorToast("用户信息修改失败！")
                                        .show(2000);
                            }
                        }
                    });
                });
    }


    @OnClick(R.id.modify_info_back)
    public void goBack(){
        onBack();
    }

}
