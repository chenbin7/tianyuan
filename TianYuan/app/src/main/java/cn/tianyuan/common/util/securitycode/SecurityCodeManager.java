package cn.tianyuan.common.util.securitycode;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class SecurityCodeManager {
    private static SecurityCodeManager sManager;
    private List<SecurityCodeTimer> list;

    public synchronized static SecurityCodeManager getInstance(){
        if(sManager == null){
            sManager = new SecurityCodeManager();
        }
        return sManager;
    }

    private SecurityCodeManager(){
        list = new ArrayList<>();
    }

    public void cancelSecurityCodeTimer(TextView btn){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getBtn() == btn){
                list.get(i).cancel();
                return;
            }
        }
    }

    public boolean startSecurityCodeTimer(int count, TextView btn){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getBtn() == btn){
                list.get(i).setCount(count);
                return true;
            }
        }
        SecurityCodeTimer timer = new SecurityCodeTimer(count, btn);
        if(timer.start()) {
            list.add(timer);
            return true;
        }
        return false;
    }

    protected void removeSecurityCodeTimer(SecurityCodeTimer timer){
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) == timer){
                index = i;
                break;
            }
        }
        if(index >= 0 && index < list.size()){
            list.remove(index);
        }
    }



}
