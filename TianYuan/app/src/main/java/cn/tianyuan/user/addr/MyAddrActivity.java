package cn.tianyuan.user.addr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.user.addr.update.UpdateAddrActivity;
import cn.tianyuan.common.view.window.ToastCheckWindow;
import cn.tianyuan.common.view.window.ToastWindow;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/10/24.
 */


public class MyAddrActivity extends BaseActivity implements IAddrUI {

    public static final String SELECT_FLAG_KEY = "select_addr";

    @BindView(R.id.no_addr)
    RelativeLayout mNoAddr;
    @BindView(R.id.addr_list)
    RecyclerView mAddrList;

    boolean selectAddr;

    MyAddrPresenter mPresenter;
    AddrAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_addr);
        ButterKnife.bind(this);
        initToolabr(getString(R.string.user_vip_my_addr));
        selectAddr = getIntent().getBooleanExtra(SELECT_FLAG_KEY, false);
        mAdapter = new AddrAdapter();
        mAddrList.setLayoutManager(new LinearLayoutManager(this));
        mAddrList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AddrAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(AddrDataBeen item, int position) {
                if(selectAddr){
                    selectAddress(item);
                }
            }

            @Override
            public void onDeteleItem(AddrDataBeen item, int position) {
                deleteAddr(item, position);
            }

            @Override
            public void onModifyItem(AddrDataBeen item, int position) {
                modifyAddr(item);
            }
        });
        mPresenter = new MyAddrPresenter(this);
        mPresenter.pullAddrList();

    }

    private void selectAddress(AddrDataBeen addr){
        Intent intent = new Intent();
        intent.putExtra("addr", addr);
        doFinish(RESULT_OK, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            AddrDataBeen tempAddr = data.getParcelableExtra("addr");
            if (requestCode == CODE_ADD) {
                mPresenter.addAddrData(tempAddr);
            } else if(requestCode == CODE_MODIFY){
                mPresenter.modifyAddrData(tempAddr);
            }
        }
    }

    final int CODE_ADD = 1;
    final int CODE_MODIFY = 2;

    public void addNewAddr(View v) {
        Intent intent = new Intent();
        intent.setClass(this, UpdateAddrActivity.class);
        doStartActivityForResult(intent, CODE_ADD);
    }

    public void deleteAddr(AddrDataBeen addr, int position){
        new ToastCheckWindow(this)
                .setTitle("删除地址")
                .setContent("确定删除地址：" + addr.fullAddress + " 吗？")
                .setPositive(getString(R.string.commit), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.deleteAddr(addr, position);
                    }
                })
                .show();
    }

    public void modifyAddr(AddrDataBeen addr) {
        Intent intent = new Intent();
        intent.putExtra("addr", addr);
        intent.setClass(this, UpdateAddrActivity.class);
        doStartActivityForResult(intent, CODE_MODIFY);
    }

    @Override
    public void onNoAddr() {
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    mNoAddr.setVisibility(View.VISIBLE);
                });
    }

    @Override
    public void onAddrList(List<AddrDataBeen> list) {
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    mNoAddr.setVisibility(View.GONE);
                    mAdapter.setData(list);
                });
    }

    @Override
    public void onDeleteAddrSucc() {
        new ToastWindow(this)
                .setRightToast("地址删除成功")
                .show(2000);
    }

    @Override
    public void onDeleteAddrFail() {
        new ToastWindow(this)
                .setErrorToast("地址删除失败")
                .show(2000);
    }
}
