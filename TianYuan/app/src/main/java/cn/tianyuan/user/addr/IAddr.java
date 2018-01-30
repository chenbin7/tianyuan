package cn.tianyuan.user.addr;

import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.user.addr.update.AddAddrResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IAddr {
    @FormUrlEncoded
    @POST("/BookLibWeb/logic/addr/getAddressList")
    Observable<AddrResponse> pullAllAddr(
            @Field("userId") String userID,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/addr/delAddress")
    Observable<SimpleResponse> deleteAddr(
            @Field("userId") String userID,
            @Field("addressId") String addressId,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/addr/addAddress")
    Observable<AddAddrResponse> addAddr(
            @Field("userId") String userID,
            @Field("address") String address,
            @Field("communityName") String communityName,
            @Field("detail") String detail,
            @Field("fullAddress") String fullAddr,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/addr/editAddress")
    Observable<SimpleResponse> modifyAddr(
            @Field("userId") String userID,
            @Field("addressId") String addressId,
            @Field("address") String address,
            @Field("communityName") String communityName,
            @Field("detail") String detail,
            @Field("fullAddress") String fullAddr,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );
}
