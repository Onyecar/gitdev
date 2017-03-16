package com.onyx.gitdev;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by onyekaanene on 15/03/2017.
 */

public class PrefUtils {
    private static final String PREF_SETUP_DONE = "done_setup";
    public static boolean isSetupDone(final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
        return sp.getBoolean(PREF_SETUP_DONE, false);
    }

    public static void markSetupDone(final boolean isDone, final Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences sp = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
        sp.edit().putBoolean(PREF_SETUP_DONE, isDone).commit();
    }
}
