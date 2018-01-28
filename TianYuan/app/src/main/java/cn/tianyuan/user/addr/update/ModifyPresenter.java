package cn.tianyuan.user.addr.update;

import android.util.Log;

import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.user.addr.AddrDataBeen;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/25.
 */

public class ModifyPresenter extends IPresenter {
    private static final String TAG = ModifyPresenter.class.getSimpleName();

    protected ModifyPresenter(IUpdateAddrUI ui) {
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
                    mModel.modifyAddr(addr, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            mUI.onModifyAddrSucc();
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            Log.w(TAG, "onFailed: "+error+"  "+msg );
                            mUI.onModifyAddrFail();
                        }
                    });
                });
    }
}
