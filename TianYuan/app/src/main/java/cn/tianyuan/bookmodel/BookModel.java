package cn.tianyuan.bookmodel;

/**
 * Created by chenbin on 2018/1/27.
 */

public class BookModel {
    private static final String TAG = BookModel.class.getSimpleName();

    private static BookModel sModel;
    private BookModel(){}
    public synchronized static BookModel getInstance(){
        if(sModel == null){
            sModel = new BookModel();
        }
        return sModel;
    }


}
