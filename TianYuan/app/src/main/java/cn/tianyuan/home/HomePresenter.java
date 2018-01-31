package cn.tianyuan.home;

import cn.tianyuan.bookmodel.BookModel;
import cn.tianyuan.common.http.HttpResultListener;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/31.
 */

public class HomePresenter {

    private HomeUI mUI;
    private BookModel mModel;


    public HomePresenter(HomeUI ui){
        mUI = ui;
        mModel = BookModel.getInstance();
    }

    public void pullChangxiaoBooks(){
        if(mModel.getChangxiaoBooks() != null){
            mUI.OnChangxiaoList(mModel.getChangxiaoBooks());
        } else {
            Observable.just(0)
                    .subscribeOn(Schedulers.io())
                    .subscribe(i -> {
                        mModel.pullChangxiaoBooks(new HttpResultListener() {
                            @Override
                            public void onSucc() {
                                if(mModel.getChangxiaoBooks() != null){
                                    mUI.OnChangxiaoList(mModel.getChangxiaoBooks());
                                }
                            }

                            @Override
                            public void onFailed(int error, String msg) {

                            }
                        });
                    });
        }
    }

    public void pullTejiaBooks(){
        if(mModel.getTejiaBooks() != null){
            mUI.OnTehuiList(mModel.getTejiaBooks());
        } else {
            Observable.just(0)
                    .subscribeOn(Schedulers.io())
                    .subscribe(i -> {
                        mModel.pullTejiaBooks(new HttpResultListener() {
                            @Override
                            public void onSucc() {
                                if(mModel.getTejiaBooks() != null){
                                    mUI.OnTehuiList(mModel.getTejiaBooks());
                                }
                            }

                            @Override
                            public void onFailed(int error, String msg) {

                            }
                        });
                    });
        }
    }

}
