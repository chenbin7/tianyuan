package cn.tianyuan.common.login;

import cn.tianyuan.common.http.Response;

/**
 * Created by chenbin on 2017/12/6.
 */

public class LoginResponse extends Response {

    public Data data;

    public class Data{
        public String id;
        public String token;
    }

    public String toString(){
        if(data != null){
            return code+","+msg+","+data.id+","+data.token;
        } else {
            return code+","+msg+"   no data";
        }
    }

}
