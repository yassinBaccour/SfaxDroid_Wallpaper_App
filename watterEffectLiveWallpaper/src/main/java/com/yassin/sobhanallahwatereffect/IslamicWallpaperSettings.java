package com.yassin.sobhanallahwatereffect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class IslamicWallpaperSettings extends PreferenceActivity implements
        Preference.OnPreferenceClickListener {

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(
                Constants.PREFERENCES_NAME);
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
                    .parse("market://details?id=com.yassin.sobhanallahwatereffect"));
            startActivity(intent);
        }
        return false;
    }

}