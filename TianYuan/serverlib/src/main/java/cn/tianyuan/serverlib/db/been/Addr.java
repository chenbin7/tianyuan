package cn.tianyuan.serverlib.db.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenbin on 2017/12/2.
 */

@Entity
public class Addr {

    @Id
    public String id;
    public String userId;
    public String addres;
    public String communityName;
    public String addrDetail;
    public String fullAddr;
    @Generated(hash = 1327604873)
    public Addr(String id, String userId, String addres, String communityName,
            String addrDetail, String fullAddr) {
        this.id = id;
        this.userId = userId;
        this.addres = addres;
        this.communityName = communityName;
        this.addrDetail = addrDetail;
        this.fullAddr = fullAddr;
    }
    @Generated(hash = 1047291667)
    public Addr() {
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
    public String getAddres() {
        return this.addres;
    }
    public void setAddres(String addres) {
        this.addres = addres;
    }
    public String getCommunityName() {
        return this.communityName;
    }
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
    public String getAddrDetail() {
        return this.addrDetail;
    }
    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }
    public String getFullAddr() {
        return this.fullAddr;
    }
    public void setFullAddr(String fullAddr) {
        this.fullAddr = fullAddr;
    }

}
