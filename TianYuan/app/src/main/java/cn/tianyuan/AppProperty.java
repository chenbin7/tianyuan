package cn.tianyuan;

/**
 * Created by Administrator on 2017/10/14.
 */

public class AppProperty {
    public enum AuthState{
        AUTH_NO,
        AUTH_ID,
        AUTH_FINISH,
        AUTH_FORBID
    }


    public static boolean hasNewVersion = false;
    public static AuthState authState = AuthState.AUTH_NO;
    public static String token = null;
    public static String userId = null;
    public static String userName = null;
    public static String account = null;

}
