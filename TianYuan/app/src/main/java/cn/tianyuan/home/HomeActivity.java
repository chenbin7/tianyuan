package cn.tianyuan.home;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.bookmodel.BookBeen;
import cn.tianyuan.search.SerachBookActivity;
import cn.tianyuan.shopcar.ShopCarActivity;
import cn.tianyuan.user.UserActivity;

/**
 * Created by Administrator on 2018/1/25.
 */

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG= HomeActivity.class.getSimpleName();

    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;

    @BindView(R.id.changxiao)
    RecyclerView mChangxiao;

    @BindView(R.id.tejia)
    RecyclerView mTejia;

    BookAdapter mChangxiaoAdapter;
    BookAdapter mTejiaAdapter;

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
        List<BookBeen> books = initData();
        mChangxiaoAdapter = new BookAdapter();
        mTejiaAdapter = new BookAdapter();
        mTejiaAdapter.setData(books);
        mChangxiaoAdapter.setData(books);
        mTejia.setAdapter(mTejiaAdapter);
        mChangxiao.setAdapter(mChangxiaoAdapter);
        mChangxiaoAdapter.setOnItemClickListener(itemListener);
        mTejiaAdapter.setOnItemClickListener(itemListener);
    }

    private BookAdapter.OnItemClickListener itemListener = new BookAdapter.OnItemClickListener() {
        @Override
        public void onRecyclerItemClick(BookBeen item, int position) {
            goBookDetailActivity(item);
        }
    };

    private List<BookBeen> initData(){
        List<BookBeen> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BookBeen book = new BookBeen();
            book.addTime = System.currentTimeMillis();
            book.descriptor = "descroptor "+i;
            book.id = "xx"+i;
            book.name = "book_"+i;
            book.price = 25;
            book.typeId = "1";
            book.userId = "1";
            book.picture = "http://img1.cache.netease.com/f2e/www/index2017/images/yuanchuang/qingsongyike.jpg";
            books.add(book);
        }
        return books;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_home:
                Log.d(TAG, "onCheckedChanged: home");
                break;
            case R.id.rbtn_category:
                Log.d(TAG, "onCheckedChanged: category");
                goActivityByClass(SerachBookActivity.class);
                break;
            case R.id.rbtn_shopcart:
                Log.d(TAG, "onCheckedChanged: shopcart");
                goActivityByClass(ShopCarActivity.class);
                break;
            case R.id.rbtn_mine:
                Log.d(TAG, "onCheckedChanged: mine");
                goActivityByClass(UserActivity.class);
                break;
            default:
                break;
        }
    }

}
