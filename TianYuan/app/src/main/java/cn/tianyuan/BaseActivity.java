package cn.tianyuan;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import cn.tianyuan.bookmodel.BookBeen;
import cn.tianyuan.detail.BookDetailActivity;
import cn.tianyuan.home.HomeActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/8/28.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;
    protected TextView mTitle;

    protected void initToolabr(String title){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.title);
        mToolbar.setTitle("");
        mTitle.setText(title);
        setSupportActionBar(mToolbar);
        initNavigation(R.drawable.icon_back, mBackListener);
    }

    protected void initNavigation(int drawId, View.OnClickListener listener){
        mToolbar.setNavigationIcon(drawId);
        mToolbar.setNavigationOnClickListener(listener);
    }

    protected void setTitle(String title){
        if(mTitle == null)
            return;
        Observable.just(title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    mTitle.setText(s);
                });
    }

    private View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBack();
        }
    };

    public void onBack(){
        doFinish(RESULT_CANCELED);
    }

    public void showKeyboard(Context mcontext,View view){
        InputMethodManager im= (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view,0);
    }

    public void toast(Context mcontext,String text){
        Toast.makeText(mcontext,text, Toast.LENGTH_SHORT).show();
    }

    public void doStartActivity(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.a_enter_in, R.anim.a_enter_out);
    }

    public void doStartActivityForResult(Intent intent, int requestCode){
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.a_enter_in, R.anim.a_enter_out);
    }

    public void doFinish(int result){
        setResult(result);
        finish();
        overridePendingTransition(R.anim.a_exit_in, R.anim.a_exit_out);
    }

    public void doFinish(int result, Intent data){
        setResult(result, data);
        finish();
        overridePendingTransition(R.anim.a_exit_in, R.anim.a_exit_out);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            doFinish(RESULT_CANCELED);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    protected int getScreenWidth(){
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    protected int getScreenHeight(){
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    //收起软键盘
    protected void hiddenKeyboard(){
        View view = this.getWindow().getDecorView();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void showSnackbar(String msg){
        View view = this.getWindow().getDecorView();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void goMainActivity(){
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        doStartActivity(intent);
        this.finish();
    }

    protected void goBookDetailActivity(BookBeen book){
        Intent intent = new Intent();
        intent.setClass(this, BookDetailActivity.class);
        intent.putExtra("book", book);
        doStartActivity(intent);
    }

    protected void goActivityByClass(Class clazz){
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        doStartActivity(intent);
    }

}
