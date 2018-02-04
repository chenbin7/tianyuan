package cn.tianyuan.orderModel;

import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.orderModel.response.OrderResponse;
import cn.tianyuan.orderModel.response.ShopCarResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface IOrder {

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/car/addIntentBook")
    Observable<SimpleResponse> addBookToCar(
            @Field("userId") String userID,
            @Field("bookId") String bookId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/car/buyBook")
    Observable<SimpleResponse> buyBook(
            @Field("intentIds") String intnetIds,
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("addrId") String addrId,
            @Field("price") int totlePrice,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/car/deteleBookFormCar")
    Observable<SimpleResponse> deteleBookFormCar(
            @Field("intentId") String intentId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/car/listIntents")
    Observable<ShopCarResponse> pullShopcarBooks(
            @Field("userid") String userID,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );


    @FormUrlEncoded
    @POST("/BookLibWeb/logic/car/listOrder")
    Observable<OrderResponse> pullOrder(
            @Field("userid") String userID,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );


    @FormUrlEncoded
    @POST("/BookLibWeb/logic/car/updateIntention")
    Observable<SimpleResponse> updateIntent(
            @Field("intentId") String intnetId,
            @Field("bookCount") int bookCount,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );




}
