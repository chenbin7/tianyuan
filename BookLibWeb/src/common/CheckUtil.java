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
        if(parms.length < count)
            return false;
        for (int i = 0; i < parms.length; i++) {
            if(parms[i] == null )
                return false;
            if(String.valueOf(parms[i]).trim().equals(""))
            	return false;
        }
        return true;
    }

    public static String getResponseBody(int code){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", "err_msg");
        System.out.println(json.toString());
        return json.toString();
    }

    public static String getResponseBody(int code, JSONObject data){
    	JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", "err_msg");
        json.put("data", data);
        System.out.println("json = "+json.toString());
        return json.toString();
    }

    public static String getResponseBody(int code, JSONArray data){
    	JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", "err_msg");
        json.put("data", data);
        System.out.println(json);
        return json.toString();
    }
    
    public static void main(String[] args) {
//    	JSONObject json = new JSONObject(); 
//    	json.put("userId", "532de969-04bc-4911-b4ca-5900fe3afc68");
//        json.put("userName", "13641983451");
//        json.put("telephone", "13641983451");
//        System.out.println("result = "+json);
//        String jString = getResponseBody(SUCC, json);
//        System.out.println(jString);
    	
    	try {
    		System.out.println("******************");
			Class clazz = Class.forName("logic.car.listOrderBooks");
			Object object = clazz.newInstance();
			System.out.println("=================="+object.getClass().getName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
}
