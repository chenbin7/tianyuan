package cn.tianyuan.user.addr.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.map.MapActivity;
import cn.tianyuan.user.addr.AddrDataBeen;
import cn.tianyuan.common.view.window.ToastCheckWindow;
import cn.tianyuan.common.view.window.ToastWindow;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/10/24.
 */

public class UpdateAddrActivity extends BaseActivity implements IUpdateAddrUI{
    private static final String TAG = UpdateAddrActivity.class.getSimpleName();

    @BindView(R.id.addr)
    TextView mAddr;
    @BindView(R.id.detail_addr)
    EditText mDetailAddr;
    @BindView(R.id.commit)
    TextView mCommit;
    String houseAddr = "";

    AddrDataBeen addr;
    IPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update_addr);
        ButterKnife.bind(this);
        initToolabr(getString(R.string.user_add_addr));
        Intent intent = getIntent();
        addr = intent.getParcelableExtra("addr");
        Log.d(TAG, "onCreate: "+addr);
        initView();
        if(addr == null){
            mPresenter = new AddPresenter(this);
            mTitle.setText(R.string.user_add_addr);
        } else {
            mPresenter = new ModifyPresenter(this);
            mTitle.setText(R.string.user_modify_addr);
            houseAddr = addr.address;
            mAddr.setText(addr.address);
            mDetailAddr.setText(addr.detail);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.init();
    }

    private void initView(){
        mAddr.addTextChangedListener(watcher);
        mDetailAddr.addTextChangedListener(watcher);
        if(addr == null) {
            mCommit.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddr.setText(houseAddr);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkEnable();
        }
    };

    private boolean mEnable = false;
    private float alpha = 0.55f;
    private void checkEnable(){
        boolean enable = checkInfo();
        Log.d(TAG, "checkEnable: ");
        if(enable == mEnable)
            return;
        mEnable = enable;
        mCommit.setEnabled(enable);
    }

    private boolean checkInfo(){
        if(mAddr.getText().toString().trim().equals(""))
            return false;
        if(mDetailAddr.getText().toString().trim().equals(""))
            return false;
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CODE_MAP && resultCode == RESULT_OK && data != null){
            AddrDataBeen tempAddr = data.getParcelableExtra("addr");
            houseAddr = tempAddr.address;
            if(addr == null){
                addr = tempAddr;
            } else {
                addr.address = tempAddr.address;
                addr.communityName = tempAddr.communityName;
            }
        }
    }

    private int CODE_MAP = 7;
    public void goMap(View v){
        Intent intent = new Intent();
        intent.setClass(this, MapActivity.class);
        doStartActivityForResult(intent, CODE_MAP);
    }

    public void commit(View v){
        String detail = mDetailAddr.getText().toString();
        addr.detail = detail;
        StringBuilder sb = new StringBuilder();
        sb.append(addr.pName)
                .append(addr.cityName)
                .append(addr.adName)
                .append(addr.address)
                .append(addr.detail);
        addr.fullAddress = sb.toString();

        new ToastCheckWindow(this)
                .setTitle("确认地址")
                .setContent(addr.fullAddress)
                .setPositive(getString(R.string.commit), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.updateAddr(addr);
                    }
                })
                .show();
    }


    @Override
    public void onAddAddrSucc(String addressId) {
        new ToastWindow(this).setRightToast("地址添加成功").show(3000);
        addr.addressId = addressId;
        Observable.just(0)
                .delay(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    Intent intent = new Intent();
                    intent.putExtra("addr", addr);
                    UpdateAddrActivity.this.doFinish(RESULT_OK, intent);
                });
    }

    @Override
    public void onAddAddrFail() {
        new ToastWindow(this).setRightToast("地址添加失败").show(3000);
    }

    @Override
    public void onModifyAddrSucc() {
        new ToastWindow(this).setRightToast("地址修改成功").show(3000);
        Observable.just(0)
                .delay(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    Intent intent = new Intent();
                    intent.putExtra("addr", addr);
                    UpdateAddrActivity.this.doFinish(RESULT_OK, intent);
                });
    }

    @Override
    public void onModifyAddrFail() {
        new ToastWindow(this).setRightToast("地址修改失败").show(3000);
    }

    @Override
    public void onError(int code, String errStr) {
        showSnackbar(errStr);
    }
}
