package cn.tianyuan.bookmodel.response;

import java.util.List;

import cn.tianyuan.common.http.Response;

/**
 * Created by chenbin on 2018/1/27.
 */

public class TypeListResponse extends Response {

    public List<Type> data;

    public class Type{
        public String id;
        public String name;
    }
}
