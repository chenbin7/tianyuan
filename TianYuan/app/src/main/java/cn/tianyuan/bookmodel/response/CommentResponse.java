package cn.tianyuan.bookmodel.response;

import java.util.List;

import cn.tianyuan.common.http.Response;

/**
 * Created by chenbin on 2017/12/10.
 */

public class CommentResponse extends Response {

    public List<Comment> data;

    public class Comment {
        public long time;
        public String userId;
        public String userName;
        public String message;
    }

}
