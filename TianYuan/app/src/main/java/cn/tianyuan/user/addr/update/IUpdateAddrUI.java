package cn.tianyuan.user.addr.update;

/**
 * Created by Administrator on 2017/10/25.
 */

public interface IUpdateAddrUI {

    public void onAddAddrSucc(String addressId);
    public void onAddAddrFail();

    public void onModifyAddrSucc();
    public void onModifyAddrFail();

    public void onError(int code, String errStr);

}
