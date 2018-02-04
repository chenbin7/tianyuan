package cn.tianyuan.user.account;

import cn.tianyuan.common.http.SimpleResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface IAccount {

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/user/logout")
    Observable<SimpleResponse> logout(
            @Field("userId") String userID,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/pwd/modify")
    Observable<SimpleResponse> modifyPasswd(
            @Field("userId") String userId,
            @Field("oldPasswd") String oldPwd,
            @Field("newPasswd") String newPwd,
            @Field("checkSum") String checkSum
    );


}
