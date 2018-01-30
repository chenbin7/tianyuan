package cn.tianyuan.mart.been;

import java.util.List;

import cn.tianyuan.common.http.Response;

/**
 * Created by Administrator on 2018/1/30.
 */

public class TypeResponse extends Response {
    public List<Type> data;

    public class Type{
        public String id;
        public String name;
    }
}
