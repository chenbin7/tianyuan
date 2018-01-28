package cn.tianyuan.common.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/4.
 */

public class CheckSum {

    private Map<String, String> map;

    public CheckSum(){
        map = new HashMap<>();
    }

    public CheckSum(String key, String value){
        map = new HashMap<>();
        if(key == null || value == null)
            return;
        map.put(key, value);
    }

    public CheckSum append(String key, int value){
        map.put(key, value+"");
        return this;
    }

    public CheckSum append(String key, long value){
        map.put(key, value+"");
        return this;
    }

    public CheckSum append(String key, String value){
        if(key == null || value == null)
            return this;
        map.put(key, value);
        return this;
    }


    public String getCheckSum(){
        Set<String> sets = map.keySet();
        Iterator<String> iterator = sets.iterator();
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        String key = "";
        for (int i = 0; i < list.size(); i++) {
            key = list.get(i);
            sb.append(key).append(map.get(key));
        }
        sb.append(Const.SALT_STRING);
        Log.d("CheckSum", "getCheckSum: "+sb.toString());
        byte[] md5 = getMD5(sb.toString());
        return  bytes2Hex(md5);
    }

    private byte[] getMD5(String val){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes("UTF-8"));
            return md5.digest();//加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String bytes2Hex(byte[] bytes) {
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
    }

}
