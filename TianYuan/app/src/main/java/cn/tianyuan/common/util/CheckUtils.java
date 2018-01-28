package cn.tianyuan.common.util;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/8/11.
 */

public class CheckUtils {
    private static final String TAG = CheckUtils.class.getSimpleName();
    public final static int PHONE_OK = 0;
    public final static int PHONE_ERR_LEN = 1;
    public final static int PHONE_ERR = 2;
    public final static int PHONE_NONE = 3;

    public final static int AUTH_CODE_OK = 0;
    public final static int AUTH_CODE_ERR = 1;
    public final static int AUTH_CODE_NONE = 2;

    public final static int PWD_OK = 0;
    public final static int PWD_ERR_DEFAULT = 1;
    public final static int PWD_ERR_LEN = 2;
    public final static int PWD_ERR_NO_LOWER = 3;
    public final static int PWD_ERR_NO_UPPER = 4;
    public final static int PWD_ERR_NO_NUM = 5;
    public final static int PWD_ERR_HAS_SPECIAL = 6;
    public final static int PWD_ERR_NONE = 7;
    public final static int PWD_ERR_NOT_SAME = -1;

    public static boolean checkIdCard(String card){
        Log.d(TAG, "checkIdCard: ");
        if(card == null)
            return false;
        if(card.trim().length() != 18)
            return false;
        if(!StrUtils.isIdCard(card)){
            return false;
        }
        String yearStr = card.substring(6, 10);
        String monthStr = card.substring(10, 12);
        String dayStr = card.substring(12, 14);
        Log.d(TAG, "checkIdCard: "+yearStr+" "+monthStr+"  "+dayStr);
        int year, month, day;
        try{
            year = Integer.parseInt(yearStr);
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
        } catch (Exception e){
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        if(year > calendar.get(Calendar.YEAR))
            return false;
        switch (month){
            case 4:
            case 6:
            case 9:
            case 11:
                if(day > 30)
                    return false;
                break;
            case 2:
                if(day > 29)
                    return false;
                break;
        }
        if(month != 2){
            return true;
        }
        if((month % 4 != 0) && (day > 28))
            return false;
        if((month % 4 == 0) && (month % 100 != 0) && (day > 28))
            return false;
        return true;
    }

    public static int checkPhoneNum(String phone){
        if(phone == null)
            return PHONE_NONE;
        if(phone.length() == 0)
            return PHONE_NONE;
        if(phone.length() != 11)
            return PHONE_ERR_LEN;
        if(StrUtils.isMobileNO(phone)){
            return PHONE_OK;
        } else {
            return PHONE_ERR;
        }
    }

    public static int checkAuthCode(String code){
        if(code == null)
            return AUTH_CODE_NONE;
        if(code.length() == 0)
            return AUTH_CODE_NONE;
        if(StrUtils.isAuthCode(code)) {
            return AUTH_CODE_OK;
        } else {
            return AUTH_CODE_ERR;
        }
    }

    public static int checkPwd(String pwd){
        if(pwd == null)
            return PWD_ERR_NONE;
        if(pwd.length() == 0)
            return PWD_ERR_NONE;
        if(pwd.length() < 6 || pwd.length() > 16)
            return PWD_ERR_LEN;
        if(StrUtils.hasSpecificChar(pwd))
            return PWD_ERR_HAS_SPECIAL;
        if(!StrUtils.hasNumber(pwd))
            return PWD_ERR_NO_NUM;
        if(!StrUtils.hasLowercase(pwd))
            return PWD_ERR_NO_LOWER;
        if(!StrUtils.hasUppercase(pwd))
            return PWD_ERR_NO_UPPER;
        return PWD_OK;
    }

}
