package cn.tianyuan.user.headerpic;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by Administrator on 2017/11/14.
 */

public interface IHeader {

    @Multipart
    @POST("/BookLibWeb/logic/user/editHeadPic")
    Observable<HeaderResponse> pushHeaderPicture(
            @Part MultipartBody.Part userId,
            @Part MultipartBody.Part photo,
            @Part MultipartBody.Part checkSum,
            @Header("token") String token
    );

}
