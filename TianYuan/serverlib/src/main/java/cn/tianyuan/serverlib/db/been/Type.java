package cn.tianyuan.serverlib.db.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenbin on 2017/12/2.
 * 书籍类别
 */

@Entity
public class Type {

    @Id
    public String id;
    public String name;
    @Generated(hash = 819209279)
    public Type(String id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 1782799822)
    public Type() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
