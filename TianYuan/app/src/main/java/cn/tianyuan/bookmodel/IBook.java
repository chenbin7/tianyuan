package cn.tianyuan.bookmodel;

import cn.tianyuan.bookmodel.response.BookListResponse;
import cn.tianyuan.bookmodel.response.CommentResponse;
import cn.tianyuan.bookmodel.response.TypeListResponse;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.user.UserInfoResponse;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface IBook {

    @Multipart
    @POST("/BookLibWeb/logic/book/addBook")
    Observable<SimpleResponse> addBook(
            @Part MultipartBody.Part userId,
            @Part MultipartBody.Part bookType,
            @Part MultipartBody.Part bookName,
            @Part MultipartBody.Part sellSum,
            @Part MultipartBody.Part bookPrice,
            @Part MultipartBody.Part bookDesc,
            @Part MultipartBody.Part bookUri,
            @Part MultipartBody.Part checkSum,
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
    @POST("/BookLibWeb/logic/book/changxiao")
    Observable<BookListResponse> pullChangxiaoBookList(
            @Field("userId") String userID,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/tehui")
    Observable<BookListResponse> pullTehuiBookList(
            @Field("userId") String userID,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/getAllBooks")
    Observable<BookListResponse> pullAllBooks(
            @Field("userId") String userID,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/booksByType")
    Observable<BookListResponse> pullTypeBookList(
            @Field("typeid") String typeId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/fraviteBooks")
    Observable<BookListResponse> pullFraviteBookList(
            @Field("userid") String userId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/addFravite")
    Observable<SimpleResponse> addFraviteBook(
            @Field("userId") String userId,
            @Field("bookId") String bookId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/deleteFrvaite")
    Observable<SimpleResponse> deleteFraviteBook(
            @Field("fraviteId") String fraviteId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/book/getAllComments")
    Observable<CommentResponse> pullBookComments(
            @Field("bookId") String bookId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

}
