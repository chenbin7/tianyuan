package cn.tianyuan.shopcar;

import cn.tianyuan.common.http.SimpleResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface IShopCar {

    @FormUrlEncoded
    @POST("/logic/car/list")
    Observable<ShopCarResponse> pullShopcarBooks(
            @Field("userId") String userID,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/car/addBook")
    Observable<SimpleResponse> addBookToCar(
            @Field("userId") String userID,
            @Field("bookId") String bookId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/car/deteleBook")
    Observable<SimpleResponse> deteleBookFormCar(
            @Field("intentId") String intentId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/car/updateIntention")
    Observable<SimpleResponse> updateIntent(
            @Field("intentId") String intnetId,
            @Field("bookCount") int bookCount,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/car/buyBook")
    Observable<SimpleResponse> buyBook(
            @Field("intentIds") String intnetIds,
            @Field("userId") String userId,
            @Field("addrId") String addrId,
            @Field("price") int totlePrice,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );


}
