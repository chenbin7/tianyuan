package cn.tianyuan.user.addr.update;


import android.util.Log;

import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.user.addr.AddrDataBeen;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/25.
 */

public class AddPresenter extends IPresenter {
    private static final String TAG = AddPresenter.class.getSimpleName();

    protected AddPresenter(IUpdateAddrUI ui) {
        super(ui);
    }

    @Override
    public void init() {

    }

    @Override
    public void updateAddr(AddrDataBeen addr) {
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.addAddr(addr, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            mUI.onAddAddrSucc(mModel.getAddressId());
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            Log.w(TAG, "onFailed: "+error+"  "+msg );
                            mUI.onAddAddrFail();
                        }
                    });
                });
    }

}
