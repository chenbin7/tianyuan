package cn.tianyuan.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import cn.tianyuan.bookmodel.BookModel;
import cn.tianyuan.bookmodel.response.BookBeen;
import cn.tianyuan.bookmodel.response.CommentResponse;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.order.OrderActivity;
import cn.tianyuan.orderModel.OrderModel;
import cn.tianyuan.orderModel.response.BookData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    BookModel mModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        mBook = getIntent().getParcelableExtra("book");
        init();
        mModel = BookModel.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.pullBookComments(mBook.id, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            List<CommentResponse.Comment> comments = mModel.getComments();
                            mAdapter.setData(comments);
                        }

                        @Override
                        public void onFailed(int error, String msg) {

                        }
                    });
                });
    }

    private void init(){
        initToolabr("详情");
        mContentList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new ContentAdapter();
        mContentList.setAdapter(mAdapter);
        ImageLoader.getInstance().displayImage(mBook.picture.trim(), mImg, TYApplication.getInstance().getOptionsBook());
        mName.setText(mBook.name);
        mDesc.setText(mBook.descriptor);
        mPrice.setText("￥:"+mBook.price/100);
    }

    @OnClick(R.id.fravite)
    public void fraviteBook(){
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.addFraviteBook(mBook.id, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            showAddFraviteToast(true);
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            showAddFraviteToast(false);
                        }
                    });
                });
    }

    private void showAddFraviteToast(boolean succ){
        Observable.just(succ)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    if(b){
                        Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddCarToast(boolean succ){
        Observable.just(succ)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    if(b){
                        Toast.makeText(getApplicationContext(), "加入购物车成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "加入购物车失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.add_to_car)
    public void addToCar(){
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    OrderModel.getInstance().addIntentBook(mBook.id, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            showAddCarToast(true);
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            showAddCarToast(false);
                        }
                    });
                });
    }

    @OnClick(R.id.buy)
    public void buyBook(){
        BookData bookData = new BookData(mBook.id, mBook.userid,mBook.typeid, mBook.name, mBook.descriptor,mBook.price,1, System.currentTimeMillis(), mBook.picture);
        bookData.intentId = mBook.id+"no";
        Intent intent = new Intent();
        intent.putExtra("price", mBook.price);
        ArrayList books = new ArrayList();
        books.add(bookData);
        intent.putParcelableArrayListExtra("books",books);
        intent.setClass(this, OrderActivity.class);
        doStartActivity(intent);
    }
}
