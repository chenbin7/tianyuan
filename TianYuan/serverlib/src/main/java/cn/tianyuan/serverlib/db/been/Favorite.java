package cn.tianyuan.serverlib.db.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenbin on 2017/12/2.
 */

@Entity
public class Favorite {

    @Id
    public String id;
    public String userId;
    public String bookId;
    @Generated(hash = 2138033399)
    public Favorite(String id, String userId, String bookId) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
    }
    @Generated(hash = 459811785)
    public Favorite() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

}
