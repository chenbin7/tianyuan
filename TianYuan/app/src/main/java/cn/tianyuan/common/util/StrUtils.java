package cn.tianyuan.common.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/8.
 */

public class StrUtils {
    public static  boolean isMobileNO(String mobiles) {
        String strPAttern = "^1[3,5,7,8][0-9]([0-9]{8})$";
        Pattern p = Pattern.compile(strPAttern);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isIdCard(String str){
        String strPattern = "^[1-9][0-9]{5}(19[0-9]{2}|20[0-3][0-9])(0[1-9]|1[0-2])[0-3][0-9][0-9]{3}[0-9,X,x]$";
        return Pattern.compile(strPattern).matcher(str).matches();
    }

    public static boolean hasChineseWord(String str){
        String strPattern = "^[\\u4e00-\\u9fa5]+.*$";
        return Pattern.compile(strPattern).matcher(str).matches();
    }

    public static boolean hasSpecificChar(String str){
        String strPattern = "^[a-z,A-Z,0-9,@,#,!,&,*,(,),;,:,=,+]+$";
        return !Pattern.compile(strPattern).matcher(str).matches();
    }

    public static boolean hasLowercase(String str){
        String strPattern = "^.*[a-z]+.*$";
        return Pattern.compile(strPattern).matcher(str).matches();
    }

    public static boolean hasUppercase(String str){
        String strPattern = "^.*[A-Z]+.*$";
        return Pattern.compile(strPattern).matcher(str).matches();
    }

    public static boolean hasNumber(String str){
        String strPattern = "^.*[0-9]+.*$";
        return Pattern.compile(strPattern).matcher(str).matches();
    }

    public static boolean isAuthCode(String str){
        String strPattern = "^([0-9]{4})$";
        return Pattern.compile(strPattern).matcher(str).matches();
    }

    public static String getMD5_Safe(String str){
        return getMD5(str+Const.SALT_STRING);
    }

    public static String getMD5(String str){
        if(str == null)
            return null;
        if(str.isEmpty())
            return null;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            byte[] bytes = md5.digest();//加密
            int iLen = bytes.length;
            // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
            int temp;
            StringBuffer sb = new StringBuffer(iLen * 2);
            for (int i = 0; i < iLen; i++) {
                temp = bytes[i] & 0x0ff;
                // 小于0F的数需要在前面补0
                if (temp < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toString(temp, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String encodeBase64(String str){
        try {
            byte[] data = Base64.encode(str.getBytes("UTF-8"), Base64.URL_SAFE|Base64.NO_WRAP);
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decodeBase64(String str){
        try {
            byte[] data = Base64.decode(str.getBytes("UTF-8"), Base64.URL_SAFE|Base64.NO_WRAP);
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }



}
