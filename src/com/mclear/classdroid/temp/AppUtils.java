package com.mclear.classdroid.temp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppUtils {

private final static String DEFAULT_PRI_URL = "def_pri_url";
private final static String DEFAULT_XP_URL = "def_xp_url";
private final static String DEFAULT_PRI_USER = "def_pri_user";
private final static String DEFAULT_XP_USER = "def_xp_user";
private final static String DEFAULT_PRI_PASS = "def_pri_pass";
private final static String DEFAULT_XP_PASS = "def_xp_pass";
	private final static String SHARED_PREFS = "settings";

	private SharedPreferences prefs;

	public AppUtils(Context context) {
		prefs = context.getSharedPreferences(SHARED_PREFS,
				Context.MODE_PRIVATE);
	}
	
	public void setDefaultPrimaryURL(String url){
		Editor edit = prefs.edit();
		edit.putString(DEFAULT_PRI_URL, url);
		edit.commit();
	}
	public void setDefaultXPURL(String url){
		Editor edit = prefs.edit();
		edit.putString(DEFAULT_XP_URL, url);
		edit.commit();
	}
	
	public String getDefaultPrimaryURL(){
		return prefs.getString(DEFAULT_PRI_URL, null);
	}
	public String getDefaultXPURL(){
		return prefs.getString(DEFAULT_XP_URL, null);
	}
	
	public void setDefaultCredsPrimary(String username, String password){
		Editor edit = prefs.edit();
		edit.putString(DEFAULT_PRI_USER, username);
		edit.putString(DEFAULT_PRI_PASS, password);
		edit.commit();
	}
	public void setDefaultCredsXP(String username, String password){
		Editor edit = prefs.edit();
		edit.putString(DEFAULT_XP_USER, username);
		edit.putString(DEFAULT_XP_PASS, password);
		edit.commit();
	}
	
	public String getDefPriUser(){
		return prefs.getString(DEFAULT_PRI_USER, "");
	}
	public String getDefPriPass(){
		return prefs.getString(DEFAULT_PRI_PASS, "");
	}
	public String getDefXPUser(){
		return prefs.getString(DEFAULT_XP_USER, "");
	}
	public String getDefXPPass(){
		return prefs.getString(DEFAULT_XP_PASS, "");
	}
}