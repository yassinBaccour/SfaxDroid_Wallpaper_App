package com.yassin.sfax.sobhanallahlwp;

import com.yassin.sfax.sobhanallahlwp.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MyPreferencesActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.livewallpaper_settings);
	}
}