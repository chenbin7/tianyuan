package cn.tianyuan.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.orderModel.OrderModel;
import cn.tianyuan.orderModel.response.BookData;
import cn.tianyuan.user.addr.AddrDataBeen;
import cn.tianyuan.user.addr.MyAddrActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2018/2/2.
 */

public class OrderActivity extends BaseActivity {
    private static final String TAG = OrderActivity.class.getSimpleName();

    @BindView(R.id.addr)
    TextView mAddr;
    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.phone)
    EditText mPhone;
    @BindView(R.id.price)
    TextView mPrice;


    OrderModel mModel;

    AddrDataBeen mAddrData;
    List<BookData> books;
    int price = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        price = intent.getIntExtra("price", 0);
        books = intent.getParcelableArrayListExtra("books");
        ButterKnife.bind(this);
        mModel = OrderModel.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAddrData != null) {
            mAddr.setText(mAddrData.fullAddress);
        }
        mPrice.setText(price / 100 + ".00");
    }

    @OnClick(R.id.commit)
    public void commit() {
        if (mAddrData == null) {
            showSnackbar("请先选择送货地址");
            return;
        }
        if (TextUtils.isEmpty(mName.getText().toString())) {
            showSnackbar("请先输入收件人姓名");
            return;
        }
        if (TextUtils.isEmpty(mName.getText().toString())) {
            showSnackbar("请先输入收件人电话");
            return;
        }
        doBuyBook();
    }

    private void doBuyBook() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < books.size(); i++) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(books.get(i).intentId);
        }
        String ids = sb.toString();
        Log.d(TAG, "doBuyBook: " + ids);
        mModel.buyBook(ids, mName.getText().toString(), mPhone.getText().toString(), mAddrData.addressId, price, new HttpResultListener() {
            @Override
            public void onSucc() {
                Log.d(TAG, "onSucc: ");
                showToast(true);
                Observable.just(0)
                        .delay(2, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> {
                            if(mModel.getIntentBooks() != null) {
                                mModel.getIntentBooks().removeAll(books);
                            }
                            doFinish(RESULT_OK);
                        });
            }

            @Override
            public void onFailed(int error, String msg) {
                showToast(false);
            }
        });
    }

    private void showToast(boolean succ) {
        Observable.just(succ)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {
                    if (b) {
                        Toast.makeText(getApplicationContext(), "订单交易成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "订单交易失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    final int CODE_ADDR = 1;

    public void selectAddr(View v) {
        Intent intent = new Intent();
        intent.setClass(this, MyAddrActivity.class);
        intent.putExtra(MyAddrActivity.SELECT_FLAG_KEY, true);
        doStartActivityForResult(intent, CODE_ADDR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_ADDR && resultCode == RESULT_OK) {
            mAddrData = data.getParcelableExtra("addr");
            Log.d(TAG, "onActivityResult: " + mAddrData);
        }
    }

}
