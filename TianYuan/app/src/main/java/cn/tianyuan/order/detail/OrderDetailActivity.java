package cn.tianyuan.order.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.order.comment.CommentActivity;
import cn.tianyuan.orderModel.OrderModel;
import cn.tianyuan.orderModel.response.BookData;
import cn.tianyuan.orderModel.response.OrderData;

/**
 * Created by Administrator on 2018/2/5.
 */

public class OrderDetailActivity extends BaseActivity {
    private static final String TAG = OrderDetailActivity.class.getSimpleName();


    @BindView(R.id.addr)
    TextView mAddr;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.phone)
    TextView mPhone;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.books)
    RecyclerView mList;

    OrderData mOrder;

    OrderModel mModel;
    OrderDetailAdapter mAdapter;
    List<BookData> mBooks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        mOrder = getIntent().getParcelableExtra("order");
        mModel = OrderModel.getInstance();
        mAdapter = new OrderDetailAdapter();
        mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OrderDetailAdapter.OnItemClickListener() {
            @Override
            public void onRecyclerItemClick(BookData item, int position) {
                goCommentActivity(item);
            }
        });
    }

    private void goCommentActivity(BookData book){
        Intent intent = new Intent();
        intent.putExtra("book", book);
        intent.setClass(this, CommentActivity.class);
        doStartActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAddr.setText(mOrder.addr);
        mName.setText(mOrder.name);
        mPhone.setText(mOrder.phone);
        mPrice.setText(mOrder.price/100 +".00");
        mModel.pullOrderDetailBooks(mOrder.id, new HttpResultListener() {
            @Override
            public void onSucc() {
                mBooks = mModel.getOrderDetailBooks();
                if(mBooks != null){
                    mAdapter.setData(mBooks);
                }
            }

            @Override
            public void onFailed(int error, String msg) {
                Log.d(TAG, "onFailed: "+error);
            }
        });
    }
}
