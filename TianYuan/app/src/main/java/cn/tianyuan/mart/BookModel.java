package cn.tianyuan.mart;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.mart.been.BookData;
import cn.tianyuan.common.util.CheckSum;
import cn.tianyuan.mart.been.TypeResponse;
import io.reactivex.functions.Consumer;

/**
 * Created by chenbin on 2017/12/10.
 */

public class BookModel {
    private static final String TAG = BookModel.class.getSimpleName();

    private static BookModel sModel;
    public synchronized static BookModel getInstance(){
        if(sModel == null){
            sModel = new BookModel();
        }
        return sModel;
    }
    private BookModel(){}

    public void addBook(@NonNull BookData data, @NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .addBook(userId, data.typeId, data.name, 10, data.price, data.desc, data.pathBase64, checkSum, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse simpleResponse) throws Exception {
                        Log.d(TAG, "addBook  accept succ: "+simpleResponse);
                        listener.check(simpleResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "addBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    List<TypeResponse.Type> types;

    public List<TypeResponse.Type> getTypes() {
        return types;
    }

    public void pullAllBookTypes(@NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .pullBookTypes(userId, checkSum, AppProperty.token)
                .subscribe(new Consumer<TypeResponse>() {
                    @Override
                    public void accept(TypeResponse response) throws Exception {
                        Log.d(TAG, "addBook  accept succ: "+response);
                        if(response.code == HttpResultListener.SUCC){
                            types = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "addBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

}
