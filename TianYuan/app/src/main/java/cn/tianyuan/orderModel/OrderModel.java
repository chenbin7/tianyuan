package cn.tianyuan.orderModel;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import cn.tianyuan.AppProperty;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.common.util.CheckSum;
import cn.tianyuan.orderModel.response.BookData;
import cn.tianyuan.orderModel.response.OrderResponse;
import cn.tianyuan.orderModel.response.ShopCarResponse;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenbin on 2018/1/27.
 */

public class OrderModel {
    private static final String TAG = OrderModel.class.getSimpleName();

    private OrderModel() {
    }

    private static OrderModel sModel;

    public synchronized static OrderModel getInstance() {
        if (sModel == null) {
            sModel = new OrderModel();
        }
        return sModel;
    }

    List<BookData> intentBooks;
    List<OrderResponse.Order> orders;


    public List<BookData> getIntentBooks() {
        return intentBooks;
    }

    public List<OrderResponse.Order> getOrders() {
        return orders;
    }

    public void pullIntentsList(@NonNull HttpResultListener listener) {
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userid", userId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IOrder.class)
                .pullShopcarBooks(userId, checkSum, AppProperty.token)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ShopCarResponse>() {
                    @Override
                    public void accept(ShopCarResponse response) throws Exception {
                        Log.d(TAG, "pullIntentsList  accept succ: " + response);
                        if (response.code == HttpResultListener.SUCC) {
                            intentBooks = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "pullIntentsList  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void addIntentBook(String bookId, @NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("bookId", bookId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IOrder.class)
                .addBookToCar(userId,bookId, checkSum, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d(TAG, "addIntentBook  accept succ: "+response);
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "addIntentBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void deleteIntentBook(String intnetId, @NonNull HttpResultListener listener){
        String checkSum = new CheckSum()
                .append("intnetId", intnetId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IOrder.class)
                .deteleBookFormCar(intnetId, checkSum, AppProperty.token)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d(TAG, "deleteIntentBook  accept succ: "+response);
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "deleteIntentBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void updateIntentBook(String intnetId, int count, @NonNull HttpResultListener listener){
        String checkSum = new CheckSum()
                .append("intnetId", intnetId)
                .append("count", count)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IOrder.class)
                .updateIntent(intnetId, count, checkSum, AppProperty.token)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d(TAG, "deleteIntentBook  accept succ: "+response);
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "deleteIntentBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void buyBook(String intentIds,String name, String phone, String addrId, int price, @NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("intentIds", intentIds)
                .append("price", price)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IOrder.class)
                .buyBook(intentIds, userId,name, phone, addrId, price, checkSum, AppProperty.token)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d(TAG, "addIntentBook  accept succ: "+response);
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "addIntentBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void pullOrderList(@NonNull HttpResultListener listener) {
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IOrder.class)
                .pullOrder(userId, checkSum, AppProperty.token)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<OrderResponse>() {
                    @Override
                    public void accept(OrderResponse response) throws Exception {
                        Log.d(TAG, "pullBooksByType  accept succ: " + response);
                        if (response.code == HttpResultListener.SUCC) {
                            orders = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "pullBooksByType  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

}
