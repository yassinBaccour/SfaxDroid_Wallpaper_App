package com.sami.rippel.besmelleh;

import rajawali.wallpaper.Wallpaper;

import android.content.Context;
import android.content.SharedPreferences;


public class MyWallpaper extends Wallpaper implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private MyWallpaperRenderer mRenderer;
    private SharedPreferences mPref;

    public Engine onCreateEngine() {
        mRenderer = new MyWallpaperRenderer(this);
        mPref = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        mPref.registerOnSharedPreferenceChangeListener(this);
        mPref.registerOnSharedPreferenceChangeListener(this);
        mRenderer.setSound(mPref.getBoolean("sound_on", false));
        if (mPref.getString("ripple_size", "small").equalsIgnoreCase("small"))
            mRenderer.setRippleSize(96);
        else if (mPref.getString("ripple_size", "medium").equalsIgnoreCase(
                "medium"))
            mRenderer.setRippleSize(72);
        else if (mPref.getString("ripple_size", "large").equalsIgnoreCase(
                "large"))
            mRenderer.setRippleSize(52);
        if (mPref.getString("ripple_speed", "slow")
                .equalsIgnoreCase("slow"))
            mRenderer.setRippleSpeed(4f);
        else if (mPref.getString("ripple_speed", "medium").equalsIgnoreCase(
                "medium"))
            mRenderer.setRippleSpeed(5.5f);
        else if (mPref.getString("ripple_speed", "fast").equalsIgnoreCase(
                "fast"))
            mRenderer.setRippleSpeed(8);
        return new WallpaperEngine(this.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE), getBaseContext(), mRenderer, false);
    }

    @Override
    public void onDestroy() {
        mPref.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
        if (mRenderer != null && key != null) {
            if (key.equalsIgnoreCase("Select_Image")) {
                mRenderer.changeBackground();
            } else if (key.equalsIgnoreCase("sound_on")) {
                mRenderer.setSound(pref.getBoolean(key, true));
            } else if (key.equalsIgnoreCase("ripple_size")) {
                if (pref.getString(key, "small").equalsIgnoreCase("small"))
                    mRenderer.setRippleSize(96);
                else if (pref.getString(key, "medium").equalsIgnoreCase(
                        "medium"))
                    mRenderer.setRippleSize(72);
                else if (pref.getString(key, "large").equalsIgnoreCase("large"))
                    mRenderer.setRippleSize(52);
            } else if (key.equalsIgnoreCase("ripple_speed")) {
                if (pref.getString(key, "slow").equalsIgnoreCase("slow"))
                    mRenderer.setRippleSpeed(4);
                else if (pref.getString(key, "medium").equalsIgnoreCase(
                        "medium"))
                    mRenderer.setRippleSpeed(5.5f);
                else if (pref.getString(key, "fast").equalsIgnoreCase("fast"))
                    mRenderer.setRippleSpeed(8);
            }
        }
    }
}
