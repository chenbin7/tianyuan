package cn.tianyuan.search;

import cn.tianyuan.bookmodel.BookModel;
import cn.tianyuan.common.http.HttpResultListener;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/31.
 */

public class SearchPresenter {

    private ISearchUI mUI;
    private BookModel mModel;


    public SearchPresenter(ISearchUI ui){
        mUI = ui;
        mModel = BookModel.getInstance();
    }

    public void pullAllBooks(){
        if(mModel.getAllBooks() != null){
            mUI.OnAllBooksList(mModel.getChangxiaoBooks());
        } else {
            Observable.just(0)
                    .subscribeOn(Schedulers.io())
                    .subscribe(i -> {
                        mModel.pullAllBooks(new HttpResultListener() {
                            @Override
                            public void onSucc() {
                                if(mModel.getChangxiaoBooks() != null){
                                    mUI.OnAllBooksList(mModel.getChangxiaoBooks());
                                }
                            }

                            @Override
                            public void onFailed(int error, String msg) {

                            }
                        });
                    });
        }
    }

    public void pullTypeBooks(String typeId){
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    mModel.pullBooksByType(typeId, new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            if(mModel.getTejiaBooks() != null){
                                mUI.OnTypeBooksList(mModel.getTypeBooks());
                            }
                        }

                        @Override
                        public void onFailed(int error, String msg) {

                        }
                    });
                });

    }

    public void pullAllTypes(){
        if(mModel.getTypes()!= null){
            mUI.onTypes(mModel.getTypes());
        } else {
            Observable.just(0)
                    .subscribeOn(Schedulers.io())
                    .subscribe(i -> {
                        mModel.pullAllBooks(new HttpResultListener() {
                            @Override
                            public void onSucc() {
                                if(mModel.getTypes()!= null){
                                    mUI.onTypes(mModel.getTypes());
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
