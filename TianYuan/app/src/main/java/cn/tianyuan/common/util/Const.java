package cn.tianyuan.common.util;

/**
 * Created by Administrator on 2017/10/9.
 */

public class Const {
    public static final String SALT_STRING = "_safe_secret_";

    public static final String SP_HAS_AUTH = "authNameId";

    public static final String PACKAGE_NAME = "cn.safeness.safe";
    public static final String WARNING_ACTIVITY_ACTION = "cn.safeness.safe.warning.activity";
    public static final String WARNING_LINK_ACTIVITY_ACTION = "cn.safeness.safe.warning.link.activity";
    public static final String LOGIN_ACTIVITY_ACTION = "cn.safeness.safe.login.activity";
    public static final long PWD_VALID_DURATION = 30*24*3600*1000;//30天
    public static final int SECURITY_CODE_TIME = 60;//秒
    public static final boolean JUST_GO_SUCC = false;
}
