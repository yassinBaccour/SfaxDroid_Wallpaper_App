package com.sami.wallpapers

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceActivity

//TODO use androidx pref
@SuppressLint("ExportedPreferenceActivity")
class SettingActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }
}