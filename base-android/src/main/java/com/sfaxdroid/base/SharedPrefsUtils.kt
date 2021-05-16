package com.sfaxdroid.base

import android.content.Context
import android.content.SharedPreferences

// TODO Inject
class SharedPrefsUtils(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constants.PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    private val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setSetting(key: String?, value: Int) {
        prefsEditor.putInt(key, value)
        prefsEditor.commit()
    }

    fun getSetting(key: String?, value: Int): Int {
        return sharedPreferences.getInt(key, value)
    }
}
