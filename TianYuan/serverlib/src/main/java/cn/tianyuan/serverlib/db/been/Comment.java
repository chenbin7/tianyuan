package cn.tianyuan.serverlib.db.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenbin on 2017/12/2.
 * 单条评论
 */


@Entity
public class Comment {

    @Id
    public String id;
    public String bookId;
    public String userId;
    public String comment;

}
