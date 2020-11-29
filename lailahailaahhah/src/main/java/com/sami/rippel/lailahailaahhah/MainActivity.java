package com.sami.rippel.lailahailaahhah;

import java.io.IOException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static long back_pressed;
    private Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences mPref = getSharedPreferences(Constants.PREFERENCESNAME,
                Context.MODE_PRIVATE);
        prefsEditor = mPref.edit();
        findViewById(R.id.buttonSetWallpaper).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ClearCurrentWallpaper();
                try {
                    Intent intent = new Intent(
                            WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(
                            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            new ComponentName(getApplicationContext(),
                                    IslamicWallpaper.class));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), R.string.txt1,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btnOtherApp).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:SFAXDROID"));
                startActivity(intent);
            }

        });

        findViewById(R.id.imageViewPub).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri
                        .parse("market://details?id=com.sami.rippel.allah"));
                startActivity(intent);
            }
        });

        RadioGroup radioWallpaper = (RadioGroup) findViewById(R.id.radioWallpaper);
        String qualitypref = mPref.getString(Constants.ChangeImageKey, "none");

        if (qualitypref.equalsIgnoreCase("Allah 1")) {
            radioWallpaper.check(R.id.wallp1);
        } else if (qualitypref.equalsIgnoreCase("Allah 2")) {
            radioWallpaper.check(R.id.wallp2);
        } else if (qualitypref.equalsIgnoreCase("Allah 3")) {
            radioWallpaper.check(R.id.wallp3);
        } else {
            radioWallpaper.check(R.id.wallp1);
            prefsEditor.putString(Constants.ChangeImageKey, "Allah 1");
            prefsEditor.apply();
        }

        radioWallpaper
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.wallp1:
                                prefsEditor.putString(Constants.ChangeImageKey,
                                        "Allah 1");
                                prefsEditor.commit();
                                break;
                            case R.id.wallp2:
                                prefsEditor.putString(Constants.ChangeImageKey,
                                        "Allah 2");
                                prefsEditor.commit();
                                break;
                            case R.id.wallp3:
                                prefsEditor.putString(Constants.ChangeImageKey,
                                        "Allah 3");
                                prefsEditor.commit();
                                break;
                        }
                    }
                });
    }

    public void ClearCurrentWallpaper() {
        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(MainActivity.this);
        try {
            myWallpaperManager.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else {
            RateUs.app_launched(this);
            Toast.makeText(getBaseContext(), R.string.txtrate6,
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}