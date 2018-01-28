package cn.tianyuan.common.login;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface ILogin {

    @FormUrlEncoded
    @POST("/logic/login/sms")
    Observable<LoginResponse> loginBySms(
            @Field("smsCode") String smsCode,
            @Field("telephone") String telephone,
            @Field("checkSum") String checkSum
    );

    @FormUrlEncoded
    @POST("/logic/login/passwd")
    Observable<LoginResponse> loginByPasswd(
            @Field("telephone") String telephone,
            @Field("passwd") String passwd,
            @Field("checkSum") String checkSum
    );
}
