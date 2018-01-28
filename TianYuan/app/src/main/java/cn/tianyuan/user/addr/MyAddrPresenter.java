package cn.tianyuan.user.addr;

import android.util.Log;
import java.util.List;

import cn.tianyuan.common.http.HttpResultListener;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/25.
 */

public class MyAddrPresenter {
    private static final String TAG = MyAddrPresenter.class.getSimpleName();

    private IAddrUI mUI;
    private MyAddrModel mModel;

    public MyAddrPresenter(IAddrUI ui){
        mUI = ui;
        mModel = new MyAddrModel();
    }

    public void addAddrData(AddrDataBeen addr){
        mModel.addAddr(addr);
        mUI.onAddrList(mModel.getAddrs());
    }
    public void deleteAddr(AddrDataBeen addr,int position) {
        Log.d(TAG, "deleteAddr: "+position);
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.deleteAddr(addr.addressId, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            mUI.onDeleteAddrSucc();
                            mModel.removeAddr(position);
                            mUI.onAddrList(mModel.getAddrs());
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            mUI.onDeleteAddrFail();
                        }
                    });
                });
    }

    public void modifyAddrData(AddrDataBeen addr){
        List<AddrDataBeen> list = mModel.getAddrs();
        if(list == null)
            return;
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).addressId.equals(addr.addressId)){
                index = i;
                break;
            }
        }
        if(index > 0 && index < list.size()){
            mModel.removeAddr(index);
            mModel.addAddr(addr);
            mUI.onAddrList(mModel.getAddrs());
        }
    }

    public void pullAddrList(){
        Log.d(TAG, "pullAddrList: ");
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.pullAllAddrList(new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            List<AddrDataBeen> list = mModel.getAddrs();
                            Log.d(TAG, "onSucc: "+list);
                            if(list == null){
                                mUI.onNoAddr();
                                return;
                            }
                            if(list.size() == 0){
                                mUI.onNoAddr();
                                return;
                            }
                            mUI.onAddrList(list);
                        }

                        @Override
                        public void onFailed(int error, String msg) {
                            mUI.onNoAddr();
                            Log.w(TAG, "onFailed: "+error+"  "+msg);
                        }
                    });
                });
    }

}
