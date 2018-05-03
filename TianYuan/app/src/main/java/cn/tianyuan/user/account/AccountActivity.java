package cn.tianyuan.user.account;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import cn.tianyuan.common.login.LoginActivity;
import cn.tianyuan.user.UserDataBeen;
import cn.tianyuan.user.account.modifyinfo.ModifyInfoActivity;
import cn.tianyuan.user.account.modifypwd.ModifyPwdActivity;
import cn.tianyuan.user.addr.MyAddrActivity;
import cn.tianyuan.common.util.PermissionUtils;
import cn.tianyuan.common.view.CircleImageView;


/**
 * Created by Administrator on 2017/10/24.
 */

public class AccountActivity extends BaseActivity implements IAccountUI {
    private static final String TAG = AccountActivity.class.getSimpleName();

    AccountPresenter mPresenter;

    @BindView(R.id.user_account)
    TextView mAccount;
    @BindView(R.id.user_header)
    CircleImageView mHeaderPic;

    UserDataBeen mUserData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        ButterKnife.bind(this);
        mUserData = getIntent().getParcelableExtra("userdata");
        mPresenter = new AccountPresenter(this);
        initToolabr(getString(R.string.user_account_info));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mUserData != null){
            mAccount.setText(mUserData.getAccount());
            String picUrl = mUserData.getUserHeadPic();
            if(!TextUtils.isEmpty(picUrl)){
                ImageLoader.getInstance().displayImage(picUrl.trim(), mHeaderPic, TYApplication.getInstance().getOptionsHeader());
            }
        }
    }

    public void logout(View v){
        mPresenter.logout();
    }

    public void modifyPwd(View v){
        Intent intent = new Intent();
        intent.setClass(AccountActivity.this, ModifyPwdActivity.class);
        doStartActivity(intent);
    }

    public void modifyInfo(View v){
        Intent intent = new Intent();
        intent.putExtra("userdata", mUserData);
        intent.setClass(AccountActivity.this, ModifyInfoActivity.class);
        doStartActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            String name = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            String sex = data.getStringExtra("sex");
            if(name != null){
                mUserData.userName = name;
            }
            if(phone != null){
                mUserData.telephone = phone;
            }
            if(mUserData.sex != null){
                mUserData.sex = sex;
            }
        }
    }

    PermissionUtils permissionUtils;
    public void goAddr(View v){
        if(permissionUtils == null){
            permissionUtils = new PermissionUtils(this);
        }
        permissionUtils.addBasePermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                new String[]{"地图需要定位权限权限"});
        permissionUtils.checkPermission(new PermissionUtils.OnPermissionResult() {
            @Override
            public void onGranted() {
                Intent intent = new Intent();
                intent.setClass(AccountActivity.this, MyAddrActivity.class);
                doStartActivity(intent);
            }

            @Override
            public void onDiened(String[] dinied) {
                showGoSettingSnackBar("照片存储需要文件读写权限");
            }
        });

    }

    private void showGoSettingSnackBar(String msg){
        Snackbar.make(mAccount, msg, Snackbar.LENGTH_SHORT)
                .setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: goSetting");
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", AccountActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        doStartActivityForResult(intent, 0);
                    }
                })
                .show();
    }

    @Override
    public void onLogout() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        doStartActivity(intent);
        this.finish();
    }

}
