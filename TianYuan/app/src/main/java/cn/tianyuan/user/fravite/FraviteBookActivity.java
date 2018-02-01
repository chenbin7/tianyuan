package cn.tianyuan.user.fravite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.bookmodel.BookModel;
import cn.tianyuan.bookmodel.response.BookBeen;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.search.TypeBookAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by chenbin on 2018/1/27.
 */

public class FraviteBookActivity extends BaseActivity {
    private static final String TAG = FraviteBookActivity.class.getSimpleName();

    @BindView(R.id.books)
    RecyclerView mBookList;

    FraviteBookAdapter mAdapter;

    BookModel mModel = BookModel.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fravite);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mBookList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new FraviteBookAdapter();
        mBookList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FraviteBookAdapter.OnItemClickListener() {
            @Override
            public void onRecyclerItemClick(BookBeen item, int position) {
                books.remove(position);
                mAdapter.notifyDataSetChanged();
                mModel.deleteFraviteBook(item.fraviteid, new HttpResultListener() {
                    @Override
                    public void onSucc() {
                        Log.d(TAG, "onSucc: delete fravite");
                    }

                    @Override
                    public void onFailed(int error, String msg) {
                        Log.d(TAG, "onFailed: delete fravite");
                    }
                });
            }
        });
    }

    List<BookBeen> books;
    @Override
    protected void onResume() {
        super.onResume();
        mModel.pullFraviteBooks(new HttpResultListener() {
            @Override
            public void onSucc() {
                 books = mModel.getFraviteBooks();
                if(books != null){
                    mAdapter.setData(books);
                }
            }

            @Override
            public void onFailed(int error, String msg) {
                Observable.just(0)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> {
                            Toast.makeText(getApplicationContext(), "您没有收藏任何书籍", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }




}
