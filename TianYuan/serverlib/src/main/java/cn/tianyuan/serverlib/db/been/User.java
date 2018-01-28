package cn.tianyuan.serverlib.db.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenbin on 2017/12/2.
 */

@Entity
public class User {

    @Id
    public String id;
    public String name;
    public String phone;
    public String passwd;
    public String header;
    @Generated(hash = 1387051978)
    public User(String id, String name, String phone, String passwd,
            String header) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.passwd = passwd;
        this.header = header;
    }
    @Generated(hash = 586692638)
    public User() {
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
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPasswd() {
        return this.passwd;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public String getHeader() {
        return this.header;
    }
    public void setHeader(String header) {
        this.header = header;
    }
}
