package cn.tianyuan.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.tianyuan.user.sell.SellBookActivity;
import cn.tianyuan.user.account.AccountActivity;
import cn.tianyuan.user.headerpic.HeaderPictureActivity;
import cn.tianyuan.common.view.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/9/5.
 */


public class UserActivity extends BaseActivity implements IUserUI {
    private static final String TAG = UserActivity.class.getSimpleName();

    private UserPresenter mPresenter;

    @BindView(R.id.header_pic)
    CircleImageView mHeaderPic;
    @BindView(R.id.account)
    TextView mAccount;

    DisplayImageOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        mPresenter = new UserPresenter(this);
        options = initImageLoaderOptions();
        mPresenter.pullUserData();
        initToolabr(getString(R.string.user_center));
    }

    public void goUserAccount(View v){
        Intent intent = new Intent();
        intent.putExtra("userdata", mPresenter.getUserData());
        intent.setClass(this, AccountActivity.class);
        doStartActivity(intent);
    }

    public void goShopCar(View v){
        Intent intent = new Intent();
//        intent.setClass(this, SuggestionActivity.class);
        doStartActivity(intent);
    }
    public void goSell(View v){
        Intent intent = new Intent();
        intent.setClass(this, SellBookActivity.class);
        doStartActivity(intent);
    }
    public void goMyOrders(View v){
        Intent intent = new Intent();
//        intent.setClass(this, NormalQuestionActivity.class);
        doStartActivity(intent);
    }
    public void goMyFravite(View v){
        Intent intent = new Intent();
//        intent.setClass(this, ContactsActivity.class);
        doStartActivity(intent);
    }

    public void goSelectHeader(View v){
        Intent intent = new Intent();
        intent.setClass(this, HeaderPictureActivity.class);
        doStartActivityForResult(intent, CODE_HEADER);
    }

    @Override
    public void onGetPic(String url) {
        Log.d(TAG, "onGetPic: "+url);
        Observable.just(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    ImageLoader.getInstance().displayImage(s, mHeaderPic, options);
                });
    }

    @Override
    public void onGetAccount(String account) {
        Observable.just(account)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    mAccount.setText(account);
                });
    }

    private final int CODE_HEADER = 7;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_HEADER && resultCode == RESULT_OK && data != null){
            String headerUri = data.getStringExtra("uri");
            Log.d(TAG, "onActivityResult: "+headerUri);
            if(headerUri != null) {
                mPresenter.setUserHeader(headerUri);
            }
        }
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
