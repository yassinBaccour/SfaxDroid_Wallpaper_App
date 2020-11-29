package com.yassin.sobhanallahwatereffect;

import rajawali.wallpaper.Wallpaper;

import android.content.Context;
import android.content.SharedPreferences;

public class IslamicWallpaper extends Wallpaper implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private IslamicWallpaperRenderer mRenderer;
    private SharedPreferences mPref;

    public Engine onCreateEngine() {
        mRenderer = new IslamicWallpaperRenderer(this);
        mPref = getSharedPreferences(Constants.PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        mPref.registerOnSharedPreferenceChangeListener(this);
        mPref.registerOnSharedPreferenceChangeListener(this);
        SetRippleSound(mPref, Constants.SOUND_KEY);
        SetRippleSize(mPref, Constants.RIPPLE_SIZE_KEY);
        SetRippleSpeedKey(mPref, Constants.RIPPLE_SPEED_KEY);
        InitBackground(mPref, Constants.CHANGE_IMAGE_KEY);
        return new WallpaperEngine(this.getSharedPreferences(
                Constants.PREFERENCES_NAME, Context.MODE_PRIVATE),
                getBaseContext(), mRenderer, false);
    }

    @Override
    public void onDestroy() {
        mPref.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
        if (mRenderer != null && key != null) {
            if (key.equalsIgnoreCase(Constants.CHANGE_IMAGE_KEY)) {
                SetRippleBackground(pref, Constants.CHANGE_IMAGE_KEY);
            } else if (key.equalsIgnoreCase(Constants.SOUND_KEY)) {
                SetRippleSound(pref, key);
            } else if (key.equalsIgnoreCase(Constants.RIPPLE_SIZE_KEY)) {
                SetRippleSize(pref, key);
            } else if (key.equalsIgnoreCase(Constants.RIPPLE_SPEED_KEY)) {
                SetRippleSpeedKey(pref, key);
            }
        }
    }

    public void SetRippleBackground(SharedPreferences pref, String key) {
        String imgKey = pref.getString(Constants.CHANGE_IMAGE_KEY, "nothing");
        if (imgKey.equalsIgnoreCase("Allah 1")) {
            mRenderer.changeBackground(R.drawable.ic_sobhanallah_wall1);
        }
        if (imgKey.equalsIgnoreCase("Allah 2")) {
            mRenderer.changeBackground(R.drawable.ic_sobhanallah_wall2);
        }
        if (imgKey.equalsIgnoreCase("Allah 3")) {
            mRenderer.changeBackground(R.drawable.ic_sobhanallah_wall3);
        }
    }

    public void InitBackground(SharedPreferences pref, String key) {
        String imgKey = pref.getString(Constants.CHANGE_IMAGE_KEY, "nothing");
        if (imgKey.equalsIgnoreCase("Allah 1")) {
            mRenderer.InitBackground(R.drawable.ic_sobhanallah_wall1);
        }
        if (imgKey.equalsIgnoreCase("Allah 2")) {
            mRenderer.InitBackground(R.drawable.ic_sobhanallah_wall2);
        }
        if (imgKey.equalsIgnoreCase("Allah 3")) {
            mRenderer.InitBackground(R.drawable.ic_sobhanallah_wall3);
        }
    }

    public void SetRippleSpeedKey(SharedPreferences pref, String key) {
        if (pref.getString(key, "slow").equalsIgnoreCase("slow"))
            mRenderer.setRippleSpeed(4f);
        else if (pref.getString(key, "medium").equalsIgnoreCase("medium"))
            mRenderer.setRippleSpeed(5.5f);
        else if (pref.getString(key, "fast").equalsIgnoreCase("fast"))
            mRenderer.setRippleSpeed(8);
    }

    public void SetRippleSize(SharedPreferences pref, String key) {
        if (pref.getString(key, "small").equalsIgnoreCase("small"))
            mRenderer.setRippleSize(96);
        else if (pref.getString(key, "medium").equalsIgnoreCase("medium"))
            mRenderer.setRippleSize(72);
        else if (pref.getString(key, "large").equalsIgnoreCase("large"))
            mRenderer.setRippleSize(52);
    }

    public void SetRippleSound(SharedPreferences pref, String key) {
        mRenderer.setSound(pref.getBoolean(key, false));
    }

}
