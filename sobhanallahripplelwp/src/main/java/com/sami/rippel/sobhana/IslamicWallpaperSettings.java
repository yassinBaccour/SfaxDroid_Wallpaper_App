package com.sami.rippel.sobhana;

import java.util.Random;

import com.sami.rippel.sobhana.R;

import rajawali.wallpaper.Wallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

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
            intent.setData(Uri.parse("market://details?id=com.sami.rippel.sobhana"));
            startActivity(intent);
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 300) {
            SharedPreferences perfs = getPreferenceManager().getSharedPreferences();
            int oldValue = perfs.getInt("Select_Image", 10000);
            Random rnd = new Random();
            int newValue = rnd.nextInt();
            while (newValue == oldValue) {
                newValue = rnd.nextInt();
            }
            perfs.edit().putInt("Select_Image", newValue).apply();
        }

    }
}