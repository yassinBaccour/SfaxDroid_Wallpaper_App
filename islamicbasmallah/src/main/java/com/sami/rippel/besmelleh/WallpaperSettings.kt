package com.sami.rippel.besmelleh

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import com.sfaxdroid.base.Utils
import rajawali.wallpaper.Wallpaper
import java.util.*

class WallpaperSettings : PreferenceActivity(),
    Preference.OnPreferenceClickListener {
    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        preferenceManager.sharedPreferencesName = Wallpaper.SHARED_PREFS_NAME
        addPreferencesFromResource(R.xml.settings)
        findPreference("recommend_1").onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (preference.key == "recommend_1") {
            Utils.ratingApplication(this)
        }
        return false
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        if (requestCode == 300) {
            val perfs =
                preferenceManager.sharedPreferences
            val oldValue = perfs.getInt("Select_Image", 10000)
            val rnd = Random()
            var newValue = rnd.nextInt()
            while (newValue == oldValue) {
                newValue = rnd.nextInt()
            }
            perfs.edit().putInt("Select_Image", newValue).apply()
        }
    }
}