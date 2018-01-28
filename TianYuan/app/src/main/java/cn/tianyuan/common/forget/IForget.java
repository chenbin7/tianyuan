package cn.tianyuan.common.forget;

import cn.tianyuan.common.http.SimpleResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface IForget {

    @FormUrlEncoded
    @POST("/logic/findback")
    Observable<SimpleResponse> findBackPasswd(
            @Field("smsCode") String smsCode,
            @Field("telephone") String telephone,
            @Field("passwd") String passwd,
            @Field("checkSum") String checkSum
    );
}
