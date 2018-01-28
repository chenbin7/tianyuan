package cn.tianyuan.user.addr.update;


import cn.tianyuan.common.http.Response;

/**
 * Created by Administrator on 2017/11/1.
 */

public class AddAddrResponse extends Response {

    public Data data;

    public class Data{
        public String addressId;
    }
}
