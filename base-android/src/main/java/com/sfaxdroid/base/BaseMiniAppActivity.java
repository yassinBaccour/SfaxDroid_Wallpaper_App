package com.sfaxdroid.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.widget.Toast;

import java.io.IOException;

public abstract class BaseMiniAppActivity extends Activity {

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else {
            RateUs.app_launched(this, this.getPackageName());
            Toast.makeText(getBaseContext(), R.string.txtrate6,
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @SuppressLint("MissingPermission")
    public void ClearCurrentWallpaper() {
        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(this);
        try {
            myWallpaperManager.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
