package cn.tianyuan.user.headerpic;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.common.http.HttpResultListener;
import cn.tianyuan.user.headerpic.cropper.CropImageView;
import cn.tianyuan.common.util.PermissionUtils;
import cn.tianyuan.common.view.window.ToastWindow;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/13.
 */

public class HeaderPictureActivity extends BaseActivity {
    private static final String TAG = HeaderPictureActivity.class.getSimpleName();

    ImageView mImg;
    CropImageView mCropImg;
    String imgPath;
    Bitmap cropBmp;
    HeaderModel mModel;
    LinearLayout mSelectBlock;
    Button mCameraBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_picture);
        mCropImg = (CropImageView) findViewById(R.id.header);
        mSelectBlock = (LinearLayout) findViewById(R.id.selector);
        mCameraBtn = (Button) findViewById(R.id.btn_camera);
        Animator anim = ObjectAnimator.ofFloat(mSelectBlock, "translationY", 700, 0).setDuration(1000);
        anim.start();
        mImg = (ImageView) findViewById(R.id.show);
        initToolabr("头像");
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.header_save){
                    saveHeader();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+imgPath);
        if(imgPath != null) {
            mCropImg.startCrop(imgPath);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_header, menu);
        return true;
    }

    public void saveHeader(){
        if(imgPath == null)
            return;
        if(mCropImg.getVisibility() == View.VISIBLE){
            mCropImg.setVisibility(View.GONE);
            mImg.setVisibility(View.VISIBLE);
            cropBmp = mCropImg.getCropBitmap();
            mImg.setImageBitmap(cropBmp);
            saveCropFileAndUpload();
        } else {
            return;
        }
    }

    private void saveCropFileAndUpload(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cropBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 95;
        while ( baos.toByteArray().length / 1024>200) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            cropBmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            Log.d(TAG, "compressImage: options:"+options+"     "+baos.toByteArray().length);
            options -= 5;//每次都减少5
        }
        String savePath = saveCropFile(baos.toByteArray());
        if(savePath == null){
            new ToastWindow(this)
                    .setErrorToast("上传头像失败")
                    .show(2000);
        } else {
            Observable.just(0)
                    .subscribeOn(Schedulers.io())
                    .subscribe(i -> {
                        if(mModel == null){
                            mModel = new HeaderModel();
                        }
                        File file = new File(savePath);
                        mModel.pushHeaderPicture(file, new HttpResultListener() {
                            @Override
                            public void onSucc() {
                                new ToastWindow(HeaderPictureActivity.this)
                                        .setRightToast("上传头像成功")
                                        .show(2000);
                                Observable.just(0)
                                        .delay(2000, TimeUnit.MILLISECONDS)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(i -> {
                                            Intent intent = new Intent();
                                            if(mModel.getHeaderUrl() != null) {
                                                Log.d(TAG, "onSucc: "+mModel.getHeaderUrl());
                                                intent.putExtra("uri", mModel.getHeaderUrl());
                                            }
                                            doFinish(RESULT_OK, intent);
                                        });
                            }

                            @Override
                            public void onFailed(int error, String msg) {
                                new ToastWindow(HeaderPictureActivity.this)
                                        .setErrorToast("上传头像失败\n"+msg)
                                        .show(2000);
                            }
                        });
                    });
        }
    }

    public String saveCropFile(byte[] data) {
        File file = new File(getFilesDir(), "header.jpg");
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

    public void cancel(View v){
        onBack();
    }

    PermissionUtils permissionUtils;
    public void getPictureFromCamera(View v){
        mCameraBtn.setEnabled(false);
        if(permissionUtils == null){
            permissionUtils = new PermissionUtils(this);
        }
        permissionUtils.addBasePermissions(
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new String[]{"拍摄认证照片需要照相机权限", "照片存储需要文件读写权限"});
        permissionUtils.checkPermission(new PermissionUtils.OnPermissionResult() {
            @Override
            public void onGranted() {
                mCameraBtn.setEnabled(true);
                mCropImg.setVisibility(View.VISIBLE);
                mSelectBlock.setVisibility(View.GONE);
                mImg.setVisibility(View.GONE);
                goTakePhotoActivity();
            }

            @Override
            public void onDiened(String[] dinied) {
                mCameraBtn.setEnabled(true);
                showGoSettingSnackBar("拍摄认证照片需要照相机权限和读写存储卡权限");
            }
        });
    }

    public void getPictureFromPhoto(View v){
        // 选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
        // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个

        if(permissionUtils == null){
            permissionUtils = new PermissionUtils(this);
        }
        permissionUtils.addBasePermissions(
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new String[]{"照片存储需要文件读写权限"});
        permissionUtils.checkPermission(new PermissionUtils.OnPermissionResult() {
            @Override
            public void onGranted() {
                mCropImg.setVisibility(View.VISIBLE);
                mSelectBlock.setVisibility(View.GONE);
                mImg.setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PHOTO);
            }

            @Override
            public void onDiened(String[] dinied) {
                showGoSettingSnackBar("照片存储需要文件读写权限");
            }
        });


    }

    private final static int REQUEST_CAMERA = 1;
    private final static int REQUEST_PHOTO = 2;
    private final static int REQUEST_SETTINGS = 3;
    Uri photoUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: "+requestCode+","+resultCode+","+data);
        if(resultCode != RESULT_OK)
            return;
        Uri selectedImage = null;
        if(requestCode == REQUEST_PHOTO){
            selectedImage = data.getData();
        } else if (requestCode == REQUEST_CAMERA){
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
                imgPath = cursor.getString(columnIndex);
                Log.d(TAG, "onActivityResult: "+imgPath);
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
        Snackbar.make(mImg, msg, Snackbar.LENGTH_SHORT)
                .setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: goSetting");
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", HeaderPictureActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        doStartActivityForResult(intent, REQUEST_SETTINGS);
                    }
                })
                .show();
    }
}
