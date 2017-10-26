package com.sami.rippel.livewallpapers.lwpwaterripple;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.Constants;

public class IslamicWallpaperSettings extends PreferenceActivity implements
        Preference.OnPreferenceClickListener {

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(
                Constants.PREFERENCESNAME);
        addPreferencesFromResource(R.xml.settings);
        findPreference("recommend_1").setOnPreferenceClickListener(this);
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("recommend_1")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri
                    .parse("market://details?id=com.sami.rippel.allah"));
            startActivity(intent);
        }
        return false;
    }
}