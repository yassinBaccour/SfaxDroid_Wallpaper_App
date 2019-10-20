package com.sami.rippel.lailahailaahhah;

import rajawali.wallpaper.Wallpaper;
import android.content.Context;
import android.content.SharedPreferences;
 

public class IslamicWallpaper extends Wallpaper implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	private IslamicWallpaperRenderer mRenderer;
	private SharedPreferences mPref;

	public Engine onCreateEngine() {
		mRenderer = new IslamicWallpaperRenderer(this);
		mPref = getSharedPreferences(Constants.PREFERENCESNAME,
				Context.MODE_PRIVATE);
		mPref.registerOnSharedPreferenceChangeListener(this);
		mPref.registerOnSharedPreferenceChangeListener(this);
		SetRippleSound(mPref, Constants.SoundKey);
		SetRippleSize(mPref, Constants.RippleSizeKey);
		SetRippleSpeedKey(mPref, Constants.RippleSpeedKey);
		InitBackground(mPref, Constants.ChangeImageKey);
		return new WallpaperEngine(this.getSharedPreferences(
				Constants.PREFERENCESNAME, Context.MODE_PRIVATE),
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
			if (key.equalsIgnoreCase(Constants.ChangeImageKey)) {
				SetRippleBackground(pref, Constants.ChangeImageKey);
			} else if (key.equalsIgnoreCase(Constants.SoundKey)) {
				SetRippleSound(pref, key);
			} else if (key.equalsIgnoreCase(Constants.RippleSizeKey)) {
				SetRippleSize(pref, key);
			} else if (key.equalsIgnoreCase(Constants.RippleSpeedKey)) {
				SetRippleSpeedKey(pref, key);
			}
		}
	}

	public void SetRippleBackground(SharedPreferences pref, String key) {
		if (pref.getString(Constants.ChangeImageKey, "nothing")
				.equalsIgnoreCase("Allah 1")) {
			mRenderer.chageBackground(R.drawable.ic_leilehaillaallah);
		}
		if (pref.getString(Constants.ChangeImageKey, "nothing")
				.equalsIgnoreCase("Allah 2")) {
			mRenderer.chageBackground(R.drawable.ic_leilehaillaallah2);
		}
		if (pref.getString(Constants.ChangeImageKey, "nothing")
				.equalsIgnoreCase("Allah 3")) {
			mRenderer.chageBackground(R.drawable.ic_leilehaillaallah3);
		}
	}

	public void InitBackground(SharedPreferences pref, String key) {
		if (pref.getString(Constants.ChangeImageKey, "nothing")
				.equalsIgnoreCase("Allah 1")) {
			mRenderer.InitBackground(R.drawable.ic_leilehaillaallah);
		}
		if (pref.getString(Constants.ChangeImageKey, "nothing")
				.equalsIgnoreCase("Allah 2")) {
			mRenderer.InitBackground(R.drawable.ic_leilehaillaallah2);
		}
		if (pref.getString(Constants.ChangeImageKey, "nothing")
				.equalsIgnoreCase("Allah 3")) {
			mRenderer.InitBackground(R.drawable.ic_leilehaillaallah3);
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
 