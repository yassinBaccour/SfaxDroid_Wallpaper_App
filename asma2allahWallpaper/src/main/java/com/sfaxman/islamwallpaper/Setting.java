package com.sfaxman.islamwallpaper;

 

import com.sfaxman.islamwallpaper.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Setting extends PreferenceActivity {
 
@SuppressWarnings("deprecation")
@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.livewallpaper_settings);
  }
} 