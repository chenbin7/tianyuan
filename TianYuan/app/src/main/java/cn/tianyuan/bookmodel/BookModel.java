package cn.tianyuan.bookmodel;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.List;

import cn.tianyuan.AppProperty;
import cn.tianyuan.bookmodel.response.BookBeen;
import cn.tianyuan.bookmodel.response.BookListResponse;
import cn.tianyuan.bookmodel.response.CommentResponse;
import cn.tianyuan.bookmodel.response.TypeListResponse;
import cn.tianyuan.common.http.HttpResource;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.common.http.SimpleResponse;
import cn.tianyuan.common.util.CheckSum;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public void addBook(@NonNull BookBeen data, @NonNull HttpResultListener listener){
        File picture = new File(data.picture);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), picture);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("bookUri", picture.getName(), requestFile);
        String userid = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userid)
                .getCheckSum();
        MultipartBody.Part userIdPart = MultipartBody.Part.createFormData("userId", userid);
        MultipartBody.Part bookTypePart = MultipartBody.Part.createFormData("bookType", data.typeid);
        MultipartBody.Part bookNamePart = MultipartBody.Part.createFormData("bookName", data.name);
        MultipartBody.Part storeSumPart = MultipartBody.Part.createFormData("storeSum", data.storesum+"");
        MultipartBody.Part bookPricePart = MultipartBody.Part.createFormData("bookPrice", data.price+"");
        MultipartBody.Part bookDescPart = MultipartBody.Part.createFormData("bookDesc", data.descriptor);
        MultipartBody.Part checkSumPart = MultipartBody.Part.createFormData("checkSum", checkSum);
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .addBook(userIdPart, bookTypePart, bookNamePart, storeSumPart, bookPricePart, bookDescPart, partFile, checkSumPart, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse simpleResponse) throws Exception {
                        Log.d(TAG, "addBook  accept succ: "+simpleResponse);
                        listener.check(simpleResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "addBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    List<TypeListResponse.Type> types;
    List<BookBeen> changxiaoBooks;
    List<BookBeen> tejiaBooks;
    List<BookBeen> allBooks;
    List<BookBeen> typeBooks;
    List<BookBeen> fraviteBooks;
    List<CommentResponse.Comment> comments;

    public List<TypeListResponse.Type> getTypes() {
        return types;
    }

    public List<BookBeen> getAllBooks() {
        return allBooks;
    }

    public List<BookBeen> getChangxiaoBooks() {
        return changxiaoBooks;
    }

    public List<BookBeen> getTejiaBooks() {
        return tejiaBooks;
    }

    public List<BookBeen> getTypeBooks() {
        return typeBooks;
    }

    public List<BookBeen> getFraviteBooks() {
        return fraviteBooks;
    }

    public List<CommentResponse.Comment> getComments() {
        return comments;
    }

    public void pullAllBookTypes(@NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .pullBookTypes(userId, checkSum, AppProperty.token)
                .subscribe(new Consumer<TypeListResponse>() {
                    @Override
                    public void accept(TypeListResponse response) throws Exception {
                        Log.d(TAG, "pullAllBookTypes  accept succ: "+response);
                        if(response.code == HttpResultListener.SUCC){
                            types = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "pullAllBookTypes  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void pullChangxiaoBooks(@NonNull HttpResultListener listener){
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .pullChangxiaoBookList(AppProperty.userId,AppProperty.token)
                .subscribe(new Consumer<BookListResponse>() {
                    @Override
                    public void accept(BookListResponse response) throws Exception {
                        Log.d(TAG, "pullChangxiaoBooks  accept succ: "+response);
                        if(response.code == HttpResultListener.SUCC){
                            changxiaoBooks = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "pullChangxiaoBooks  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void pullTejiaBooks(@NonNull HttpResultListener listener){
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .pullTehuiBookList(AppProperty.userId,AppProperty.token)
                .subscribe(new Consumer<BookListResponse>() {
                    @Override
                    public void accept(BookListResponse response) throws Exception {
                        Log.d(TAG, "pullTejiaBooks  accept succ: "+response);
                        if(response.code == HttpResultListener.SUCC){
                            tejiaBooks = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "pullTejiaBooks  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void pullAllBooks(@NonNull HttpResultListener listener){
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .pullAllBooks(AppProperty.userId,AppProperty.token)
                .subscribe(new Consumer<BookListResponse>() {
                    @Override
                    public void accept(BookListResponse response) throws Exception {
                        Log.d(TAG, "pullAllBooks  accept succ: "+response);
                        if(response.code == HttpResultListener.SUCC){
                            allBooks = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "pullAllBooks  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void pullBooksByType(String typeId, @NonNull HttpResultListener listener){
        String checkSum = new CheckSum()
                .append("typeId", typeId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .pullTypeBookList(typeId, checkSum, AppProperty.token)
                .subscribe(new Consumer<BookListResponse>() {
                    @Override
                    public void accept(BookListResponse response) throws Exception {
                        Log.d(TAG, "pullBooksByType  accept succ: "+response);
                        if(response.code == HttpResultListener.SUCC){
                            typeBooks = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "pullBooksByType  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void pullFraviteBooks(@NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .pullFraviteBookList(userId, checkSum, AppProperty.token)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BookListResponse>() {
                    @Override
                    public void accept(BookListResponse response) throws Exception {
                        Log.d(TAG, "addBook  accept succ: "+response);
                        if(response.code == HttpResultListener.SUCC){
                            fraviteBooks = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "addBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void addFraviteBook(String bookId, @NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("bookId", bookId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .addFraviteBook(userId,bookId, checkSum, AppProperty.token)
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d(TAG, "addFraviteBook  accept succ: "+response);
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "addFraviteBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void deleteFraviteBook(String fraviteId, @NonNull HttpResultListener listener){
        String checkSum = new CheckSum()
                .append("fraviteId", fraviteId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .deleteFraviteBook(fraviteId, checkSum, AppProperty.token)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d(TAG, "deleteFraviteBook  accept succ: "+response);
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "deleteFraviteBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void pullBookComments(String bookId, @NonNull HttpResultListener listener){
        String checkSum = new CheckSum()
                .append("bookId", bookId)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .pullBookComments(bookId, checkSum, AppProperty.token)
                .subscribe(new Consumer<CommentResponse>() {
                    @Override
                    public void accept(CommentResponse response) throws Exception {
                        Log.d(TAG, "pullBookComments  accept succ: "+response);
                        if(response.code == HttpResultListener.SUCC){
                            comments = response.data;
                        }
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "pullBookComments  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

    public void addComment(String bookId, String comment, @NonNull HttpResultListener listener){
        String userId = AppProperty.userId;
        String checkSum = new CheckSum()
                .append("userId", userId)
                .append("bookId", bookId)
                .append("comment", comment)
                .getCheckSum();
        HttpResource.getInstance()
                .getRetrofit()
                .create(IBook.class)
                .addComment(userId,bookId, comment, checkSum, AppProperty.token)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) throws Exception {
                        Log.d(TAG, "addFraviteBook  accept succ: "+response);
                        listener.check(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "addFraviteBook  accept:  fail");
                        listener.check(throwable);
                    }
                });
    }

}
