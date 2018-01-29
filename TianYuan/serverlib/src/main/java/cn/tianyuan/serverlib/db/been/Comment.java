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
    @Generated(hash = 1634027372)
    public Comment(String id, String bookId, String userId, String comment) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.comment = comment;
    }
    @Generated(hash = 1669165771)
    public Comment() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

}
