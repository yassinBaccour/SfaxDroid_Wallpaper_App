package com.sami.rippel.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sami.rippel.model.Constants;

/**
 * Created by yassin baccour on 15/04/2017.
 */

public class SharedPrefsUtils {
    private Context mContext;
    private SharedPreferences mPref;
    private SharedPreferences.Editor prefsEditor;

    public SharedPrefsUtils(Context mContext) {
        this.mContext = mContext;
        mPref = mContext.getSharedPreferences(Constants.PREFERENCESNAME,
                Context.MODE_PRIVATE);
        prefsEditor = mPref.edit();
    }

    public void SetSetting(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public void SetSetting(String key, int value) {
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public void SetSetting(String key, Boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public String GetSetting(String key, String value) {
        return mPref.getString(key, value);
    }

    public int GetSetting(String key, int value) {
        return mPref.getInt(key, value);
    }

    public boolean GetSetting(String key, boolean value) {
        return mPref.getBoolean(key, value);
    }

}
