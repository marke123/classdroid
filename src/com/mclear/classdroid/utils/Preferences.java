
package com.mclear.classdroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {
    private static final String SHARED_PREFS_FILE = "prefs";
    
    private static final String IS_FIRST_RUN = "is_first_run";

    private SharedPreferences preferences;

    private Editor editor;

    public Preferences(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }
    
    public void setFirstRun(boolean value){
        editor = preferences.edit();
        editor.putBoolean(IS_FIRST_RUN, value);
        editor.commit();
    }
    
    public boolean isFirstRun(){
        return preferences.getBoolean(IS_FIRST_RUN, true);
    }
}
