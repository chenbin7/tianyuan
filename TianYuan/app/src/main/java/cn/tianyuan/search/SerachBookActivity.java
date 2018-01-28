package cn.tianyuan.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.bookmodel.BookBeen;
import cn.tianyuan.common.view.picker.PickerUtils;

/**
 * Created by chenbin on 2018/1/27.
 */

public class SerachBookActivity extends BaseActivity {
    private static final String TAG = SerachBookActivity.class.getSimpleName();

    @BindView(R.id.books)
    RecyclerView mBookList;

    TypeBookAdapter mAdapter = new TypeBookAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mBookList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new TypeBookAdapter();
        mAdapter.setData(initData());
        mBookList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TypeBookAdapter.OnItemClickListener() {
            @Override
            public void onRecyclerItemClick(BookBeen item, int position) {
                goBookDetailActivity(item);
            }
        });

        types = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            types.add("types_"+i);
        }
    }

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

    @OnClick(R.id.choice_type)
    public void onChoice(){
        PickerUtils.onStringArray(this, types, new PickerUtils.PickerResultListener() {
            @Override
            public void onResult(String result, Object... detailedParms) {
                Log.d(TAG, "onResult: "+detailedParms);
            }
        });
    }

    private List<String> types;

}
