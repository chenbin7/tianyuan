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
import cn.tianyuan.common.login.LoginActivity;
import cn.tianyuan.user.UserDataBeen;
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
    DisplayImageOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        ButterKnife.bind(this);
        mUserData = getIntent().getParcelableExtra("userdata");
        mPresenter = new AccountPresenter(this);
        options = initImageLoaderOptions();
        initToolabr(getString(R.string.user_account_info));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mUserData != null){
            mAccount.setText(mUserData.getAccount());
            String picUrl = mUserData.getUserHeadPic();
            if(!TextUtils.isEmpty(picUrl)){
                ImageLoader.getInstance().displayImage(picUrl, mHeaderPic, options);
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


    private DisplayImageOptions initImageLoaderOptions(){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.user_header) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.user_header)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.user_header)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(mHeaderPic.getWidth() / 2))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
        return options;
    }
}
