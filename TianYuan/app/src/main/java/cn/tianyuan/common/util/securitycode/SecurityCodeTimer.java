package cn.tianyuan.common.util.securitycode;

import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/8/11.
 */

class SecurityCodeTimer {
    private int count;
    private TextView btn;

    protected TextView getBtn() {
        return btn;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    protected SecurityCodeTimer(int count, TextView btn){
        this.count = count;
        this.btn = btn;
    }

    protected boolean start(){
        if(count <= 0){
            return false;
        }
        if(btn == null){
            return false;
        }
        countDownApplyAuthCode();
        return true;
    }

    private void countDownApplyAuthCode(){
        if(count <= 0){
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( i -> {
                        btn.setText(R.string.security_code);
                        btn.setEnabled(true);
                    });
            SecurityCodeManager.getInstance().removeSecurityCodeTimer(this);
            return;
        }
        Observable.just(count)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(c -> {
                    btn.setEnabled(false);
                    btn.setText(TYApplication.getAppContext().getString(R.string.apply_security_code_delay, count));
                })
                .delay(1, TimeUnit.SECONDS)
                .subscribe(c -> {
                    --count;
                    countDownApplyAuthCode();
                });
    }

    protected void cancel(){
        count = -1;
    }

}
