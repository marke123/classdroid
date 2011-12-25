
package com.mclear.classdroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ExportUtils {
    private final static String PREF_FILE = "export";

    private final static String FIRST_RUN = "first_run";

    private SharedPreferences preferences;

    public ExportUtils(Context context) {
        preferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        boolean isFirstRun = preferences.getBoolean(FIRST_RUN, true);

        if (isFirstRun) {
            Editor editor = preferences.edit();
            editor.putBoolean(FIRST_RUN, false);
        }
        return isFirstRun;
    }
}
