package com.sami.rippel.lailahailaahhah;

import rajawali.wallpaper.Wallpaper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class IslamicWallpaperSettings extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(Wallpaper.SHARED_PREFS_NAME);
        addPreferencesFromResource(R.xml.settings);
        findPreference("recommend_1").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("recommend_1")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.sami.rippel.lailahailaahhah"));
            startActivity(intent);
        }
        return false;
    }

}