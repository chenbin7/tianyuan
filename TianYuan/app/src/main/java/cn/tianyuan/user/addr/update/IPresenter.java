package cn.tianyuan.user.addr.update;

import cn.tianyuan.user.addr.AddrDataBeen;

/**
 * Created by Administrator on 2017/10/25.
 */

public abstract class IPresenter {
    protected IUpdateAddrUI mUI;
    protected  UpdateAddrModel mModel;

    protected IPresenter(IUpdateAddrUI ui){
        this.mUI = ui;
        mModel = new UpdateAddrModel();
    }

    public abstract void init();
    public abstract void updateAddr(AddrDataBeen addr);

}
