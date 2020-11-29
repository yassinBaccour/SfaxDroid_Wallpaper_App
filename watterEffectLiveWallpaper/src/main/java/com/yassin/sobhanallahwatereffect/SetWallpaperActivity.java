package com.yassin.sobhanallahwatereffect;

import java.io.IOException;

import java.util.Locale;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SetWallpaperActivity extends Activity {
    private static long back_pressed;
    public Button btnSetWallpaper;
    public Boolean isAdsShow = false;
    public SharedPreferences sharedPreferences;
    private Editor prefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(Constants.PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        btnSetWallpaper = findViewById(R.id.btnSetWallpaper);
        btnSetWallpaper.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ClearCurrentWallpaper();
                try {
                    isAdsShow = true;
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
        findViewById(R.id.btotherapp).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                isAdsShow = true;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:SFAXDROID"));
                startActivity(intent);
            }

        });

        RadioGroup radioWallpaper = findViewById(R.id.radioChooseWallpaper);
        String qualityPref = sharedPreferences.getString(Constants.CHANGE_IMAGE_KEY, "none");

        if (qualityPref.equalsIgnoreCase("Allah 1")) {
            radioWallpaper.check(R.id.wallp1);
        } else if (qualityPref.equalsIgnoreCase("Allah 2")) {
            radioWallpaper.check(R.id.wallp2);
        } else if (qualityPref.equalsIgnoreCase("Allah 3")) {
            radioWallpaper.check(R.id.wallp3);
        } else {
            radioWallpaper.check(R.id.wallp1);
            prefsEditor.putString(Constants.CHANGE_IMAGE_KEY, "Allah 1");
            prefsEditor.apply();
        }

        radioWallpaper
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.wallp1:
                                prefsEditor.putString(Constants.CHANGE_IMAGE_KEY,
                                        "Allah 1");
                                prefsEditor.commit();
                                break;
                            case R.id.wallp2:
                                prefsEditor.putString(Constants.CHANGE_IMAGE_KEY,
                                        "Allah 2");
                                prefsEditor.commit();
                                break;
                            case R.id.wallp3:
                                prefsEditor.putString(Constants.CHANGE_IMAGE_KEY,
                                        "Allah 3");
                                prefsEditor.commit();
                                break;
                        }
                    }
                });


        ImageView imageViewPub = findViewById(R.id.imageViewPub);

        final String pk;
        boolean isAr = Locale.getDefault().getLanguage().contentEquals("ar");
        if (isAr) {
            imageViewPub.setImageDrawable(getResources().getDrawable(R.drawable.ic_pub2));
            pk = "market://details?id=com.islama.worldstickers";
        } else {
            imageViewPub.setImageDrawable(getResources().getDrawable(R.drawable.ic_pub));
            pk = "market://details?id=com.sami.rippel.allah";
        }
        imageViewPub.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri
                        .parse(pk));
                startActivity(intent);
            }
        });
    }

    public void ClearCurrentWallpaper() {
        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(SetWallpaperActivity.this);
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