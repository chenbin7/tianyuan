package cn.tianyuan.home;

import java.util.List;

import cn.tianyuan.bookmodel.response.BookBeen;

/**
 * Created by Administrator on 2018/1/31.
 */

public interface HomeUI {

    public void OnChangxiaoList(List<BookBeen> books);
    public void OnTehuiList(List<BookBeen> books);

}
