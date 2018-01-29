package common;

import logic.register;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CheckUtil {
	
	private static final String TAG = CheckUtil.class.getSimpleName();

    public static final int SUCC = 200;
    public static final int ERR_COMMON = 10000;
    public static final int ERR_UNKNOW = 10010;
    public static final int ERR_PARAM = 10011;
    public static final int ERR_ACCOUNT_EXIT = 10012;
    public static final int ERR_USER_NO_REG = 10013;
    public static final int ERR_LOGIN_INVALID = 10014;
	
	public static boolean checkParamsNotNull(int count, String ...parms){
        if(count <= 0)
            return false;
        if(parms == null)
            return false;
        if(parms.length != count)
            return false;
        for (int i = 0; i < parms.length; i++) {
            if(parms[i] == null )
                return false;
            if(String.valueOf(parms[i]).trim().equals(""))
            	return false;
        }
        return true;
    }

    public static JSON getResponseBody(int code){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", "err_msg");
        System.out.println(json.toString());
        return json;
    }

    public static JSONObject getResponseBody(int code, JSONObject data){
    	JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", "err_msg");
        json.put("data", data);
        System.out.println(json);
        return json;
    }

    public static JSONObject getResponseBody(int code, JSONArray data){
    	JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", "err_msg");
        json.put("data", data);
        System.out.println(json);
        return json;
    }
}
