package cn.tianyuan.user;

import cn.tianyuan.common.http.SimpleResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface IUser {



    @FormUrlEncoded
    @POST("/logic/user/modifyPasswd")
    Observable<SimpleResponse> modifyPwd(
            @Field("userId") String userID,
            @Field("oldPasswd") String oldPasswd,
            @Field("newPasswd") String newPasswd,
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
