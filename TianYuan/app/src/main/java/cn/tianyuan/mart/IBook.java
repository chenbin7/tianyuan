package cn.tianyuan.mart;

import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.mart.been.TypeResponse;
import cn.tianyuan.user.UserInfoResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface IBook {

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/addBook")
    Observable<SimpleResponse> addBook(
            @Field("userId") String userID,
            @Field("bookType") String type,
            @Field("bookName") String name,
            @Field("sellSum") int sellSum,
            @Field("bookPrice") int price,
            @Field("bookDesc") String desc,
            @Field("bookUri") String path,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/getBookTypes")
    Observable<TypeResponse> pullBookTypes(
            @Field("userId") String userID,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );


}
