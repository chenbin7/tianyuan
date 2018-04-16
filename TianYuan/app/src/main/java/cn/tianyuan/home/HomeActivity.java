package cn.tianyuan.home;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.bookmodel.response.BookBeen;
import cn.tianyuan.search.SearchBookActivity;
import cn.tianyuan.shopcar.ShopCarActivity;
import cn.tianyuan.user.UserActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/25.
 */

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, HomeUI {
    private static final String TAG= HomeActivity.class.getSimpleName();

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    @BindView(R.id.rbtn_home)
    RadioButton mHome;
    @BindView(R.id.rbtn_category)
    RadioButton mType;
    @BindView(R.id.rbtn_shopcart)
    RadioButton mShopcar;
    @BindView(R.id.rbtn_mine)
    RadioButton mMine;

    @BindView(R.id.changxiao)
    RecyclerView mChangxiao;

    @BindView(R.id.tejia)
    RecyclerView mTejia;

    @BindView(R.id.ad)
    ImageView mAd;

    BookAdapter mChangxiaoAdapter;
    BookAdapter mTejiaAdapter;

    HomePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        init();
    }

    private void init(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mChangxiao.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTejia.setLayoutManager(linearLayoutManager2);
        mChangxiaoAdapter = new BookAdapter();
        mTejiaAdapter = new BookAdapter();
        mTejia.setAdapter(mTejiaAdapter);
        mChangxiao.setAdapter(mChangxiaoAdapter);
        mChangxiaoAdapter.setOnItemClickListener(itemListener);
        mTejiaAdapter.setOnItemClickListener(itemListener);
        mPresenter = new HomePresenter(this);
    }

    Timer timer;
    int timerCount = 0;
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        mPresenter.pullChangxiaoBooks();
        mPresenter.pullTejiaBooks();
        timer = new Timer();
        timerCount = 0;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                    ++timerCount;
                    int imgsrc = R.drawable.ad1;
                    switch (timerCount % 3){
                        case 0: imgsrc = R.drawable.ad1;break;
                        case 1: imgsrc = R.drawable.ad2;break;
                        case 2: imgsrc = R.drawable.ad3;break;
                    }
                    Observable.just(imgsrc)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(id -> {
                                mAd.setImageResource(id);
                            });
                }
        }, 1000, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRadioGroup.check(R.id.rbtn_home);
    }

    private BookAdapter.OnItemClickListener itemListener = new BookAdapter.OnItemClickListener() {
        @Override
        public void onRecyclerItemClick(BookBeen item, int position) {
            goBookDetailActivity(item);
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_home:
                Log.d(TAG, "onCheckedChanged: home");
                break;
            case R.id.rbtn_category:
                Log.d(TAG, "onCheckedChanged: category");
                if(mType.isChecked()) {
                    goActivityByClass(SearchBookActivity.class);
                }
                break;
            case R.id.rbtn_shopcart:
                Log.d(TAG, "onCheckedChanged: shopcart");
                if(mShopcar.isChecked()) {
                    goActivityByClass(ShopCarActivity.class);
                }
                break;
            case R.id.rbtn_mine:
                Log.d(TAG, "onCheckedChanged: mine");
                if(mMine.isChecked()) {
                    goActivityByClass(UserActivity.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnChangxiaoList(List<BookBeen> books) {
        mChangxiaoAdapter.setData(books);
    }

    @Override
    public void OnTehuiList(List<BookBeen> books) {
        mTejiaAdapter.setData(books);
    }
}
