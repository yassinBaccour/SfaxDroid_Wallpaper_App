package com.sfaxdroid.engine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.sfaxdroid.mini.base.Utils

class WallpaperSettings : AppCompatActivity() {

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, MyPreferenceFragment())
            .commit()
    }
}

class MyPreferenceFragment : PreferenceFragmentCompat(),
    androidx.preference.Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferencesName = Constants.PREFERENCE_NAME
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    }

    override fun onPreferenceChange(
        preference: androidx.preference.Preference?,
        newValue: Any?
    ): Boolean {
        if (preference?.key == "rate_us") {
            Utils.ratingApplication(requireContext())
        }
        return false
    }
}