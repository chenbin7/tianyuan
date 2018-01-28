package cn.tianyuan.user.addr;

import java.util.List;

/**
 * Created by Administrator on 2017/10/25.
 */

public interface IAddrUI {
    public void onNoAddr();
    public void onAddrList(List<AddrDataBeen> list);

    public void onDeleteAddrSucc();
    public void onDeleteAddrFail();

}
