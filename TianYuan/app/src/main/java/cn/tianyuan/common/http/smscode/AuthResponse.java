package cn.tianyuan.common.http.smscode;


import cn.tianyuan.common.http.Response;

/**
 * Created by Administrator on 2017/10/11.
 */

public class AuthResponse extends Response {

    public Data data;

    public class Data{
        public int code;
    }
}
