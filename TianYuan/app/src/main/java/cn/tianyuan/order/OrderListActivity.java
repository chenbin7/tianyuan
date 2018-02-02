package cn.tianyuan.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.orderModel.OrderModel;
import cn.tianyuan.orderModel.response.OrderResponse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2018/2/2.
 */

public class OrderListActivity extends BaseActivity {

    @BindView(R.id.list)
    RecyclerView mRecyclerList;

    OrderAdapter mAdapter;
    OrderModel mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mAdapter = new OrderAdapter();
        mRecyclerList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerList.setAdapter(mAdapter);
        mModel = OrderModel.getInstance();
    }

    List<OrderResponse.Order> orders;
    @Override
    protected void onStart() {
        super.onStart();
        mModel.pullOrderList(new HttpResultListener() {
            @Override
            public void onSucc() {
                orders = mModel.getOrders();
                if(orders != null){
                    mAdapter.setData(orders);
                }
            }

            @Override
            public void onFailed(int error, String msg) {
                Observable.just(0)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> {
                            Toast.makeText(getApplicationContext(), "您目前没有订单", Toast.LENGTH_LONG).show();
                        });
            }
        });
    }
}