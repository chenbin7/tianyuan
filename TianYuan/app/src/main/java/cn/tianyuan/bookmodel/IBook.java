package cn.tianyuan.bookmodel;

import cn.tianyuan.bookmodel.response.BookListResponse;
import cn.tianyuan.bookmodel.response.CommentResponse;
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
    Observable<TypeListResponse> pullBookTypes(
            @Field("userId") String userID,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLinWeb/logic/book/changxiao")
    Observable<BookListResponse> pullChangxiaoBookList(
            @Field("userId") String userID,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLinWeb/logic/book/tehui")
    Observable<BookListResponse> pullTehuiBookList(
            @Field("userId") String userID,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLinWeb/logic/book/allBooks")
    Observable<BookListResponse> pullAllBooks(
            @Field("userId") String userID,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLinWeb/logic/book/booksByType")
    Observable<BookListResponse> pullTypeBookList(
            @Field("typeId") String typeId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLinWeb/logic/book/fraviteBooks")
    Observable<BookListResponse> pullFraviteBookList(
            @Field("userId") String userId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLinWeb/logic/book/addFravite")
    Observable<SimpleResponse> addFraviteBook(
            @Field("userId") String userId,
            @Field("bookId") String bookId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLinWeb/logic/book/deleteFravite")
    Observable<SimpleResponse> deleteFraviteBook(
            @Field("fraviteId") String fraviteId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLinWeb/logic/book/getAllComments")
    Observable<CommentResponse> pullBookComments(
            @Field("bookId") String bookId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

}
