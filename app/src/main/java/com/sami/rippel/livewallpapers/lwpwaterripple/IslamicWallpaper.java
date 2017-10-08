package com.sami.rippel.livewallpapers.lwpwaterripple;
import rajawali.wallpaper.Wallpaper;
import android.content.Context;
import android.content.SharedPreferences;
import com.sami.rippel.model.Constants;

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
		SetRippleSound(mPref, Constants.SOUND_KEY);
		SetRippleSize(mPref, Constants.RIPPLE_SIZE_KEY);
		SetRippleSpeedKey(mPref, Constants.RIPPLE_SPEED_KEY);
		return new WallpaperEngine(this.getSharedPreferences(
				Constants.PREFERENCESNAME, Context.MODE_PRIVATE),
				getBaseContext(), mRenderer, false);
	}

	@Override
	public void onDestroy() {
		if (mPref != null)
		mPref.unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
		if (mRenderer != null && key != null) {
			if (key.equalsIgnoreCase(Constants.SOUND_KEY)) {
				SetRippleSound(pref, key);
			} else if (key.equalsIgnoreCase(Constants.RIPPLE_SIZE_KEY)) {
				SetRippleSize(pref, key);
			} else if (key.equalsIgnoreCase(Constants.RIPPLE_SPEED_KEY)) {
				SetRippleSpeedKey(pref, key);
			}
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
