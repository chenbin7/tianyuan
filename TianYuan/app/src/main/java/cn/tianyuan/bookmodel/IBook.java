package cn.tianyuan.bookmodel;

import cn.tianyuan.bookmodel.response.BookListResponse;
import cn.tianyuan.bookmodel.response.TypeListResponse;
import cn.tianyuan.common.http.SimpleResponse;
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
    @POST("/logic/book/typelist")
    Observable<TypeListResponse> pullTypeList(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/book/changxiao")
    Observable<BookListResponse> pullChangxiaoBookList(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/book/tehui")
    Observable<BookListResponse> pullTehuiBookList(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/book/list_by_type")
    Observable<BookListResponse> pullTehuiBookList(
            @Field("typeId") String typeId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/book/list_fravite")
    Observable<BookListResponse> pullFraviteBookList(
            @Field("userId") String userId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/book/addFravite")
    Observable<SimpleResponse> addFraviteBook(
            @Field("userId") String userId,
            @Field("bookId") String bookId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/logic/book/deletefravite")
    Observable<SimpleResponse> deleteFraviteBook(
            @Field("fravite") String FraviteId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );


    @FormUrlEncoded
    @POST("/logic/user/getInfo")
    Observable<UserInfoResponse> pullUserInfo(
            @Field("userId") String userID,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );


}
