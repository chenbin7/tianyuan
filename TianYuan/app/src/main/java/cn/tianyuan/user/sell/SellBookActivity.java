package cn.tianyuan.user.sell;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.bookmodel.response.BookBeen;
import cn.tianyuan.bookmodel.BookModel;
import cn.tianyuan.bookmodel.response.TypeListResponse;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.serverlib.server.UUIDUtil;
import cn.tianyuan.common.util.PermissionUtils;
import cn.tianyuan.common.util.StrUtils;
import cn.tianyuan.common.view.picker.PickerUtils;
import cn.tianyuan.common.view.window.ToastWindow;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chenbin on 2017/12/10.
 */

public class SellBookActivity extends BaseActivity {
    private static final String TAG = SellBookActivity.class.getSimpleName();

    @BindView(R.id.book_pic)
    ImageView mBookImg;
    @BindView(R.id.book_name)
    EditText mName;
    @BindView(R.id.book_sum)
    EditText mSum;
    @BindView(R.id.book_price)
    EditText mPrice;
    @BindView(R.id.book_desc)
    EditText mDescriptor;
    @BindView(R.id.book_type)
    TextView mType;

    Bitmap mShowBmp;
    String savePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_sell);
        ButterKnife.bind(this);
        initToolabr("我要卖书");
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mShowBmp != null){
            mBookImg.setImageBitmap(mShowBmp);
        }
    }

    public void onSellCommit(View v){
        if(TextUtils.isEmpty(savePath)){
            showSnackbar("请先拍摄一张图书照片");
            return;
        }
        String name = mName.getText().toString();
        if(TextUtils.isEmpty(name)){
            showSnackbar("请先输入书名");
            return;
        }
        String sum = mSum.getText().toString();
        if(TextUtils.isEmpty(name)){
            showSnackbar("请先输入书本数量");
            return;
        }
        String priceStr = mPrice.getText().toString();
        if(TextUtils.isEmpty(priceStr)){
            showSnackbar("请先输入价格");
            return;
        }
        double p = Double.parseDouble(priceStr);
        int price = (int)p * 100;
        if(price <= 0){
            showSnackbar("请先输入正确的价格");
            return;
        }
        String type = mType.getText().toString();
        if(TextUtils.isEmpty(type)){
            showSnackbar("请先选择图书类别");
            return;
        }
        String desc = mDescriptor.getText().toString();
        if(TextUtils.isEmpty(type)){
            showSnackbar("请先输入图书的描述信息");
            return;
        }
        BookBeen book = new BookBeen();
        book.descriptor = desc;
        book.name = name;
        book.storesum = Integer.parseInt(sum);
        book.price = price;
        book.typeid = typeId;
        book.picture = savePath;
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    BookModel.getInstance()
                            .addBook(book, new HttpResultListener() {
                                @Override
                                public void onSucc() {
                                    new ToastWindow(SellBookActivity.this)
                                            .setRightToast("图书添加成功！")
                                            .show(2000);
                                    Observable.just(0)
                                            .delay(2000, TimeUnit.MILLISECONDS)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(i -> {
                                                doFinish(RESULT_OK);
                                            });
                                }

                                @Override
                                public void onFailed(int error, String msg) {
                                    deletePicFile(savePath);
                                    new ToastWindow(SellBookActivity.this)
                                            .setErrorToast("图书添加失败！")
                                            .show(2000);
                                }
                            });
                });
    }

    private void deletePicFile(String path){
        if(path == null)
            return;
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    private void init(){
        types = new ArrayList<>();
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> {
                    BookModel.getInstance().pullAllBookTypes(new HttpResultListener() {
                        @Override
                        public void onSucc() {
                            booksTypes = BookModel.getInstance().getTypes();
                            if(booksTypes != null && booksTypes.size() > 0){
                                for (int i = 0; i < booksTypes.size(); i++) {
                                    types.add(booksTypes.get(i).name);
                                }
                            }
                        }

                        @Override
                        public void onFailed(int error, String msg) {

                        }
                    });
                });
    }

    PermissionUtils permissionUtils;
    public void onTakePhotoBook(View v){
        if(permissionUtils == null){
            permissionUtils = new PermissionUtils(this);
        }
        permissionUtils.addBasePermissions(
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new String[]{"拍摄认证照片需要照相机权限", "照片存储需要文件读写权限"});
        permissionUtils.checkPermission(new PermissionUtils.OnPermissionResult() {
            @Override
            public void onGranted() {
                goTakePhotoActivity();
            }

            @Override
            public void onDiened(String[] dinied) {
                showGoSettingSnackBar("拍摄认证照片需要照相机权限和读写存储卡权限");
            }
        });
    }


    List<String> types = null;
    List<TypeListResponse.Type> booksTypes;
    String typeId = "";
    public void onSelectotType(View v){
        PickerUtils.onStringArray(this, types, new PickerUtils.PickerResultListener() {
            @Override
            public void onResult(String result, Object... detailedParms) {
                Observable.just(result)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            mType.setText(result);
                            if(detailedParms.length > 0) {
                                int position = (int) detailedParms[0];
                                if(position > 0 && position < booksTypes.size()){
                                    typeId = booksTypes.get(position).id;
                                }
                            }
                        });
            }
        });
    }

    private final static int REQUEST_CAMERA = 1;
    private final static int REQUEST_SETTINGS = 3;
    Uri photoUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: "+requestCode+","+resultCode+","+data);
        if(resultCode != RESULT_OK)
            return;
        Uri selectedImage = null;
        if (requestCode == REQUEST_CAMERA){
            selectedImage = photoUri;
        }
        if(selectedImage != null){
            Cursor cursor = null;
            try{
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //picturePath就是图片在储存卡所在的位置
                String imgPath = cursor.getString(columnIndex);
                Log.d(TAG, "onActivityResult: "+imgPath);
                scaleBitmap(imgPath);
            } catch (Exception e){

            } finally {
                if(cursor != null){
                    cursor.close();
                    cursor = null;
                }
            }
        }
    }

    /***
     * 相机权限  ***********************************************************************************
     */

    public void goTakePhotoActivity(){
        Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /***
         * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
         * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
         * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
         */
        ContentValues values = new ContentValues();
        photoUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(getImageByCamera, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(permissions, grantResults);
    }

    private void showGoSettingSnackBar(String msg){
        Snackbar.make(mBookImg, msg, Snackbar.LENGTH_SHORT)
                .setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: goSetting");
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", SellBookActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        doStartActivityForResult(intent, REQUEST_SETTINGS);
                    }
                })
                .show();
    }


    private void scaleBitmap(String impPath){
        Log.d(TAG, "scaleBitmap: "+impPath);
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = false;
        bmpOptions.inSampleSize = 4;   //width，hight设为原来的 1/4
        mShowBmp = BitmapFactory.decodeFile(impPath, bmpOptions);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mShowBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 95;
        while ( baos.toByteArray().length / 1024>200) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            mShowBmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            Log.d(TAG, "compressImage: options:"+options+"     "+baos.toByteArray().length);
            options -= 5;//每次都减少5
        }
        savePath = saveCropFile(baos.toByteArray());
        deletePicFile(impPath);
    }

    public String saveCropFile(byte[] data) {
        File file = new File(getFilesDir(), UUIDUtil.getUUID()+"_book.jpg");
        Log.w(TAG, "onTakePhoto: dir:" + file.getAbsolutePath() + "    " + data.length);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            String savePath = file.getAbsolutePath();
            return savePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
        }
    }

}
