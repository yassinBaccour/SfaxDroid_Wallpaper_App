package com.sfaxdroid.engine

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import com.sfaxdroid.mini.base.Utils
import java.util.*

class WallpaperSettings : PreferenceActivity(),
    Preference.OnPreferenceClickListener {

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        preferenceManager.sharedPreferencesName = Constants.PREFERENCE_NAME
        addPreferencesFromResource(R.xml.settings)
        findPreference("rate_us").onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (preference.key == "rate_us") {
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
            val sharedPreferences =
                preferenceManager.sharedPreferences
            val oldValue = sharedPreferences.getInt(Constants.CHANGE_IMAGE_KEY, 10000)
            val rnd = Random()
            var newValue = rnd.nextInt()
            while (newValue == oldValue) {
                newValue = rnd.nextInt()
            }
            sharedPreferences.edit().putInt(Constants.CHANGE_IMAGE_KEY, newValue).apply()
        }
    }
}