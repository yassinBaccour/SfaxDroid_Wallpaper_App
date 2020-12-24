package com.yassin.sfax.tawakkolalaallah

import android.os.Bundle
import android.preference.PreferenceActivity
import com.sfaxdroid.mini.base.Constans

//TODO use androidx pref
class SettingActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferencesName = Constans.PREF_NAME;
        addPreferencesFromResource(R.xml.settings)
    }
}