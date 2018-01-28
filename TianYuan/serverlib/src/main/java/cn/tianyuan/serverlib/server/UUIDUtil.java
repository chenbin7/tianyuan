package cn.tianyuan.serverlib.server;

import java.util.UUID;

/**
 * Created by chenbin on 2017/12/5.
 */

public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

}
