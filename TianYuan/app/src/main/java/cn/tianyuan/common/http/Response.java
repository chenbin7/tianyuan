package cn.tianyuan.common.http;

/**
 * Created by Administrator on 2017/9/4.
 */

public abstract class Response {
    public static final int OK = 200;

    public int code;
    public String msg;

    public Response() {
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String toString(){
        return code+":"+msg;
    }
}
