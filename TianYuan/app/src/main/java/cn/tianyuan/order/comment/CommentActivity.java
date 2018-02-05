package cn.tianyuan.order.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import cn.tianyuan.bookmodel.BookModel;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.orderModel.response.BookData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2018/2/5.
 */

public class CommentActivity extends BaseActivity {

    @BindView(R.id.book_pic)
    ImageView mImage;
    @BindView(R.id.book_name)
    TextView mName;
    @BindView(R.id.book_price)
    TextView mPrice;
    @BindView(R.id.book_desc)
    TextView mDescriptor;
    @BindView(R.id.comment)
    EditText mComment;

    BookData mBook;

    BookModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_coment);
        ButterKnife.bind(this);
        mBook = getIntent().getParcelableExtra("book");
        mModel = BookModel.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageLoader.getInstance().displayImage(mBook.picture, mImage, TYApplication.getInstance().getOptions());
        mName.setText(mBook.name);
        mPrice.setText("￥:"+mBook.price/100+".00");
        mDescriptor.setText(mBook.descriptor);
    }

    @OnClick(R.id.commit)
    public void onCommit(){
        String comment = mComment.getText().toString();
        if(TextUtils.isEmpty(comment)){
            Toast.makeText(getApplicationContext(), "请输入您的评价！", Toast.LENGTH_SHORT).show();
            return;
        }
        mModel.addComment(mBook.id, comment, new HttpResultListener() {
            @Override
            public void onSucc() {
                Observable.just(0)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> {
                            Toast.makeText(getApplicationContext(), "评价成功", Toast.LENGTH_SHORT).show();
                            mComment.setText("");
                        });
            }

            @Override
            public void onFailed(int error, String msg) {
                Observable.just(0)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> {
                            Toast.makeText(getApplicationContext(), "评价失败", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
