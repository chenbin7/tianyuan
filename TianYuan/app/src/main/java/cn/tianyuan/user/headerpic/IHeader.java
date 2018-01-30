package cn.tianyuan.user.headerpic;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


/**
 * Created by Administrator on 2017/11/14.
 */

public interface IHeader {

    @FormUrlEncoded
    @POST("/BookLibWeb/logic/user/editHeadPic")
    Observable<HeaderResponse> pushHeaderPicture(
            @Field("userId") String userId,
            @Field("fileUri") String fileUri,
            @Field("checkSum") String checkSum,
            @Header("token") String token
    );

}
