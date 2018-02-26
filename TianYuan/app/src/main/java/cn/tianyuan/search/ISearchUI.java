package cn.tianyuan.search;

import java.util.List;

import cn.tianyuan.bookmodel.response.BookBeen;
import cn.tianyuan.bookmodel.response.TypeListResponse;

/**
 * Created by Administrator on 2018/1/31.
 */

public interface ISearchUI {
    public void OnAllBooksList(List<BookBeen> books);
    public void OnTypeBooksList(List<BookBeen> books);
    public void onTypes(List<TypeListResponse.Type> types);
    public void onError(String msg);
}
