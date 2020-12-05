package com.yassin.sfax.tawakkolalaallah

import android.os.Bundle
import android.preference.PreferenceActivity

class SettingActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }
}