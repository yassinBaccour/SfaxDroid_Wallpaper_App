package com.sami.rippel.lailahailaahhah;

import java.util.Random;

import com.sami.rippel.lailahailaahhah.R;

import rajawali.wallpaper.Wallpaper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class IslamicWallpaperSettings extends PreferenceActivity implements Preference.OnPreferenceClickListener {

	private final String TAG="Skyfall Preference";
	private final int SELECT_IMAGE = 300;

	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getPreferenceManager().setSharedPreferencesName(Wallpaper.SHARED_PREFS_NAME);
		addPreferencesFromResource(R.xml.settings);
		findPreference("recommend_1").setOnPreferenceClickListener(this);
 
		//getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onDestroy() {
	//	getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}

	
	

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		if(preference.getKey().equals("recommend_1"))
		{
			Intent intent = new Intent(Intent.ACTION_VIEW); 
			intent.setData(Uri.parse("market://details?id=com.sami.rippel.lailahailaahhah")); 
			startActivity(intent);
		}  
		return false;
	}
 
}