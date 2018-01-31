package cn.tianyuan.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import cn.tianyuan.bookmodel.response.BookBeen;

/**
 * Created by chenbin on 2018/1/27.
 */

public class BookDetailActivity extends BaseActivity {
    private static final String TAG = BookDetailActivity.class.getSimpleName();

    @BindView(R.id.book_pic)
    ImageView mImg;
    @BindView(R.id.book_name)
    TextView mName;
    @BindView(R.id.book_price)
    TextView mPrice;
    @BindView(R.id.book_desc)
    TextView mDesc;
    @BindView(R.id.book_suggestion)
    RecyclerView mContentList;

    BookBeen mBook;

    ContentAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        mBook = getIntent().getParcelableExtra("book");
        init();
    }

    private void init(){
        initToolabr("详情");
        mContentList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new ContentAdapter();
        mAdapter.setData(infos());
        mContentList.setAdapter(mAdapter);
        ImageLoader.getInstance().displayImage(mBook.picture, mImg, TYApplication.getInstance().getOptions());
        mName.setText(mBook.name);
        mDesc.setText(mBook.descriptor);
        mPrice.setText("￥:"+mBook.price);
    }

    private List<ContentInfo> infos(){
        List<ContentInfo> infos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ContentInfo info = new ContentInfo();
            info.time = System.currentTimeMillis();
            info.name = "name"+i;
            info.content= ";alodrgnoa;erhgnro;ng;aoriegb___"+i;
            infos.add(info);
        }
        return infos;
    }

    @OnClick(R.id.fravite)
    public void fraviteBook(){}

    @OnClick(R.id.add_to_car)
    public void addToCar(){}

    @OnClick(R.id.buy)
    public void buyBook(){}
}
