
package com.primaryt.classdroid.temp;

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

    private static final String SHARED_PREFS_FILE = "prefs";

    private static final String IS_FIRST_RUN = "is_first_run";

    private final static String SHARED_PREFS = "settings";
    
    private final static String NEW_URL = "new_url";
    
    private final static String NEW_USERNAME = "new_username";
    
    private final static String NEW_PASSWORD = "new_password";

    private SharedPreferences prefs;

    public AppUtils(Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void setDefaultPrimaryURL(String url) {
        Editor edit = prefs.edit();
        edit.putString(DEFAULT_PRI_URL, url);
        edit.commit();
    }

    public void setDefaultXPURL(String url) {
        Editor edit = prefs.edit();
        edit.putString(DEFAULT_XP_URL, url);
        edit.commit();
    }

    public String getDefaultPrimaryURL() {
        return prefs.getString(DEFAULT_PRI_URL, null);
    }

    public String getDefaultXPURL() {
        return prefs.getString(DEFAULT_XP_URL, null);
    }

    public void setDefaultCredsPrimary(String username, String password) {
        Editor edit = prefs.edit();
        edit.putString(DEFAULT_PRI_USER, username);
        edit.putString(DEFAULT_PRI_PASS, password);
        edit.commit();
    }

    public void setDefaultCredsXP(String username, String password) {
        Editor edit = prefs.edit();
        edit.putString(DEFAULT_XP_USER, username);
        edit.putString(DEFAULT_XP_PASS, password);
        edit.commit();
    }

    public String getDefPriUser() {
        return prefs.getString(DEFAULT_PRI_USER, "");
    }

    public String getDefPriPass() {
        return prefs.getString(DEFAULT_PRI_PASS, "");
    }

    public String getDefXPUser() {
        return prefs.getString(DEFAULT_XP_USER, "");
    }

    public String getDefXPPass() {
        return prefs.getString(DEFAULT_XP_PASS, "");
    }

    public void setFirstRun(boolean value) {
        Editor edit = prefs.edit();
        edit.putBoolean(IS_FIRST_RUN, value);
        edit.commit();
    }

    public boolean isFirstRun() {
        return prefs.getBoolean(IS_FIRST_RUN, true);
    }
    
    public String getNewURL(){
    	return prefs.getString(NEW_URL, "http://demopupil.primaryblogger.co.uk/");
    }
    
    public String getNewUsername(){
    	return prefs.getString(NEW_USERNAME, null);
    }
    
    public String getNewPassword(){
    	return prefs.getString(NEW_PASSWORD, null);
    }
    
    public void setNewURL(String url){
    	Editor edit = prefs.edit();
    	edit.putString(NEW_URL, url);
    	edit.commit();
    }
    
    public void setNewUsername(String username){
    	Editor edit = prefs.edit();
    	edit.putString(NEW_USERNAME, username);
    	edit.commit();
    }
    
    public void setNewPassword(String password){
    	Editor edit = prefs.edit();
    	edit.putString(NEW_PASSWORD, password);
    	edit.commit();
    }
}
