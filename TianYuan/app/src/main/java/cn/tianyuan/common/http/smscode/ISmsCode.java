package cn.tianyuan.common.http.smscode;

import cn.tianyuan.common.http.SimpleResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface ISmsCode {

    @FormUrlEncoded
    @POST("/logic/sms/getSmsCode")
    Observable<SimpleResponse> apply(
            @Field("type") int type,
            @Field("telephone") String telephone,
            @Field("checkSum") String checkSum
    );

    @FormUrlEncoded
    @POST("/logic/user/getSmsCode")
    Observable<AuthResponse> auth(
            @Field("telephone") String telephone,
            @Field("smsCode") String smsCode,
            @Field("checkSum") String checkSum
    );

}
