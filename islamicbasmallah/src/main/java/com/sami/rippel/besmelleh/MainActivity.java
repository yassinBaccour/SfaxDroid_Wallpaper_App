package com.sami.rippel.besmelleh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static long back_pressed;
    public Boolean isAdsShow = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttonSetWallpaper).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                isAdsShow = true;
                try {
                    Intent intent = new Intent(
                            WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(
                            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            new ComponentName(getApplicationContext(),
                                    MyWallpaper.class));
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

        findViewById(R.id.buttonRating).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                isAdsShow = true;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri
                        .parse("market://details?id=com.sami.rippel.besmelleh"));
                startActivity(intent);
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
        findViewById(R.id.imageViewPub).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri
                        .parse("market://details?id=com.sami.rippel.allah"));
                startActivity(intent);
            }
        });
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