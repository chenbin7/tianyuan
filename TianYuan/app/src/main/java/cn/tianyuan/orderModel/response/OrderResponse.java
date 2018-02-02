package cn.tianyuan.orderModel.response;

import java.util.List;

import cn.tianyuan.common.http.Response;

/**
 * Created by Administrator on 2018/2/2.
 */

public class OrderResponse extends Response {

    public List<Order> data;

    public class Order{
        public String id;
        public long time;
        public int price;
        public String addr;
        public String name;
        public String phone;
    }
}
