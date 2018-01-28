package cn.tianyuan.user.account;

import cn.tianyuan.common.util.AndroidSharedPreferences;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/24.
 */

public class AccountPresenter {

    private AccountModel mModel;
    private IAccountUI mUI;

    public AccountPresenter(IAccountUI ui){
        mUI = ui;
        mModel = new AccountModel();
    }

    public void logout(){
        AndroidSharedPreferences.getInstance().logout();
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i ->{
                    mModel.logout();
                });
        mUI.onLogout();
    }
}
