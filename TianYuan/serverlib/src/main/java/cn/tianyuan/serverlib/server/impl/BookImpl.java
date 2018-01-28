package cn.tianyuan.serverlib.server.impl;


import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import java.util.List;
import cn.tianyuan.serverlib.db.been.Book;
import cn.tianyuan.serverlib.db.been.Type;
import cn.tianyuan.serverlib.db.greendao.BookDao;
import cn.tianyuan.serverlib.db.greendao.TypeDao;
import cn.tianyuan.serverlib.server.IHttpServer;
import cn.tianyuan.serverlib.server.UUIDUtil;

/**
 * Created by chenbin on 2017/12/5.
 */

public class BookImpl extends IHttpServer {
    private static final String TAG = IHttpServer.class.getSimpleName();

    private String getTypeIdByName(String name){
        TypeDao typeDao = getDBSession().getTypeDao();
        List<Type> list = typeDao.queryBuilder().where(TypeDao.Properties.Name.eq("其他")).list();
        if(list != null && list.size() > 0){
            return list.get(0).getId();
        } else {
            String id = UUIDUtil.getUUID();
            Type type = new Type(id, "其他");
            typeDao.insert(type);
            return id;
        }
    }

    public void addBook(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        doAnalysisRequest(request);
        String userId = parms.getString("userId");
        String type = parms.getString("bookType");
        String name = parms.getString("bookName");
        String priceStr = parms.getString("bookPrice");
        String desc = parms.getString("bookDesc");
        String uri = parms.getString("bookUri");
        if(!checkParamsNotNull(5, userId, name, priceStr, desc, uri)){
            response.send(getResponseBody(ERR_PARAM));
            return;
        }
        if(getUserByI(userId) == null){
            response.send(getResponseBody(ERR_ACCOUNT_EXIT));
            return;
        }
        int price = Integer.parseInt(priceStr);
        Book book = new Book(UUIDUtil.getUUID(), userId, type, name, desc, price,0,1, System.currentTimeMillis(), uri);
        BookDao bookDao = getDBSession().getBookDao();
        long result = bookDao.insert(book);
        if(result > 0){
            response.send(getResponseBody(SUCC));
        } else {
            response.send(getResponseBody(ERR_COMMON));
        }
    }
}
