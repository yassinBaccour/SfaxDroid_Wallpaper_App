package com.sami.abmmodeljdid

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceActivity

@SuppressLint("ExportedPreferenceActivity")
class WallpaperPreferencesSetting : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.live_wallpaper_settings)
    }
}