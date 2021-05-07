package com.sfaxdroid.base

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by yassin baccour on 15/04/2017.
 */
class SharedPrefsUtils(mContext: Context) {
    private val mPref: SharedPreferences = mContext.getSharedPreferences(
        Constants.PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    private val prefsEditor: SharedPreferences.Editor = mPref.edit()
    fun SetSetting(key: String?, value: String?) {
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun SetSetting(key: String?, value: Int) {
        prefsEditor.putInt(key, value)
        prefsEditor.commit()
    }

    fun SetSetting(key: String?, value: Boolean?) {
        prefsEditor.putBoolean(key, value!!)
        prefsEditor.commit()
    }

    fun GetSetting(key: String?, value: String?): String? {
        return mPref.getString(key, value)
    }

    fun GetSetting(key: String?, value: Int): Int {
        return mPref.getInt(key, value)
    }

    fun GetSetting(key: String?, value: Boolean): Boolean {
        return mPref.getBoolean(key, value)
    }
}
