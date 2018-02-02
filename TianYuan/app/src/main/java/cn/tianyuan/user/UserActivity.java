package cn.tianyuan.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import cn.tianyuan.order.OrderListActivity;
import cn.tianyuan.shopcar.ShopCarActivity;
import cn.tianyuan.user.fravite.FraviteBookActivity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        mPresenter = new UserPresenter(this);
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
        intent.setClass(this, ShopCarActivity.class);
        doStartActivity(intent);
    }
    public void goSell(View v){
        Intent intent = new Intent();
        intent.setClass(this, SellBookActivity.class);
        doStartActivity(intent);
    }
    public void goMyOrders(View v){
        Intent intent = new Intent();
        intent.setClass(this, OrderListActivity.class);
        doStartActivity(intent);
    }
    public void goMyFravite(View v){
        Intent intent = new Intent();
        intent.setClass(this, FraviteBookActivity.class);
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
                    ImageLoader.getInstance().displayImage(s.trim(), mHeaderPic, TYApplication.getInstance().getOptions());
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

}
