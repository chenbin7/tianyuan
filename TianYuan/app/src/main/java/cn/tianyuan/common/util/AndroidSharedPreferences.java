package cn.tianyuan.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AndroidSharedPreferences {
	private static final String TAG = AndroidSharedPreferences.class.getSimpleName();

	private SharedPreferences sharePreference;
//	private SharedPreferences.Editor editor;
	public final static String PREF_NAME = "safeness";
	public final static String AUTH = "login_auto_auth";
	public final static String PHONE = "login_auto_phone";
	public final static String TOKEN = "login_auto_token";

	private static AndroidSharedPreferences sAsp;
	public synchronized static AndroidSharedPreferences getInstance() {
		if(sAsp == null){
			sAsp = new AndroidSharedPreferences();
		}
		return sAsp;
	}

	public void init(Context context){
		sharePreference = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
//		editor = sharePreference.edit();
	}

	private AndroidSharedPreferences(){}

	public void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = sharePreference.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = sharePreference.edit();
		editor.putString(key, value);
		boolean reuslt = editor.commit();
		Log.d(TAG, "putString: "+key+"   "+value+"   "+reuslt);
	}
	
	public void putLong(String key ,long value){
		SharedPreferences.Editor editor = sharePreference.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public void putInt(String key, int value) {
		SharedPreferences.Editor editor = sharePreference.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public boolean getBoolean(String key,boolean defaultValue){
		 return sharePreference.getBoolean(key, defaultValue);
	}
	
	public String getString(String key,String defValue){
		String value = sharePreference.getString(key, defValue);
		Log.d(TAG, "getString: "+key+"   "+value+"   "+defValue);
		return value;
	}
	
	public int getInt(String key,int defValue){
		return sharePreference.getInt(key, defValue);
	}
	
	public long getLong(String key,long defValue){
		return sharePreference.getLong(key, defValue);
	}

	public void logout() {
		sharePreference.edit()
				.remove(PHONE)
				.remove(AUTH)
				.remove(TOKEN)
				.commit();
	}
}
