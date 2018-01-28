package cn.tianyuan.user.headerpic;

import cn.tianyuan.common.http.Response;

/**
 * Created by Administrator on 2017/11/14.
 */

public class HeaderResponse extends Response {

    public Data data;

    public class Data{
        String headPicUrl;
    }
}
