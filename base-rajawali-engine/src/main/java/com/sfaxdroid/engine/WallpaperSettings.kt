package com.sfaxdroid.engine

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import java.util.*

class WallpaperSettings : PreferenceActivity(),
    Preference.OnPreferenceClickListener {

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        preferenceManager.sharedPreferencesName = Constants.PREFERENCE_NAME
        addPreferencesFromResource(R.xml.settings)
        findPreference("recommend_1").onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (preference.key == "recommend_1") {
            //Utils.ratingApplication(this)
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
            val oldValue = sharedPreferences.getInt("Select_Image", 10000)
            val rnd = Random()
            var newValue = rnd.nextInt()
            while (newValue == oldValue) {
                newValue = rnd.nextInt()
            }
            sharedPreferences.edit().putInt("Select_Image", newValue).apply()
        }
    }
}