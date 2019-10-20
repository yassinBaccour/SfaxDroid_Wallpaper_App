package com.sami.abmmodeljdid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SetWallpaperActivity extends Activity {

    private static long back_pressed;
    private Editor prefsEditor;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);
        initAdMob();
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        prefsEditor = sharedPrefs.edit();

        RadioGroup mAnimationRadioBtn = findViewById(R.id.radio_animation);
        RadioGroup mQualityBtn = findViewById(R.id.radio_quality);

        findViewById(R.id.button_set_wallpaper).setOnClickListener(new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(
                            WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(
                            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            new ComponentName(getApplicationContext(),
                                    ServiceNameOfAllah.class));
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

        findViewById(R.id.button_open_gallery).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ViewWallpaperActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_privacy).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iubenda.com/privacy-policy/27298346"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        findViewById(R.id.btn_rating).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SetWallpaperActivity.this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.btn_other_app).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:SFAXDROID"));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SetWallpaperActivity.this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        String animationPref = sharedPrefs.getString("prefSyncFrequency", "none");
        if (animationPref.equalsIgnoreCase("speed1")) {
            mAnimationRadioBtn.check(R.id.speed1);
        } else if (animationPref.equalsIgnoreCase("speed2")) {
            mAnimationRadioBtn.check(R.id.speed2);
        } else if (animationPref.equalsIgnoreCase("speed3")) {
            mAnimationRadioBtn.check(R.id.speed3);
        } else if (animationPref.equalsIgnoreCase("speed4")) {
            mAnimationRadioBtn.check(R.id.speed4);
        } else {
            mAnimationRadioBtn.check(R.id.speed3);
            prefsEditor.putString("prefSyncFrequency", "speed3");
            prefsEditor.apply();
        }

        mAnimationRadioBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.speed1:
                        prefsEditor.putString("prefSyncFrequency", "speed1");
                        prefsEditor.commit();
                        break;
                    case R.id.speed2:
                        prefsEditor.putString("prefSyncFrequency", "speed2");
                        prefsEditor.commit();
                        break;
                    case R.id.speed3:
                        prefsEditor.putString("prefSyncFrequency", "speed3");
                        prefsEditor.commit();
                        break;
                    case R.id.speed4:
                        prefsEditor.putString("prefSyncFrequency", "speed4");
                        prefsEditor.commit();
                        break;
                }
            }
        });

        String qualityPref = sharedPrefs.getString("prefQuality", "none");
        if (qualityPref.equalsIgnoreCase("quality1")) {
            mQualityBtn.check(R.id.quality1);
        } else if (qualityPref.equalsIgnoreCase("quality2")) {
            mQualityBtn.check(R.id.quality2);
        } else {
            mQualityBtn.check(R.id.quality1);
            prefsEditor.putString("prefquality", "quality1");
            prefsEditor.commit();
        }

        mQualityBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.quality1:
                        prefsEditor.putString("prefquality", "quality1");
                        prefsEditor.commit();
                        break;
                    case R.id.quality2:
                        prefsEditor.putString("prefquality", "quality2");
                        prefsEditor.commit();
                        break;
                }
            }
        });
    }

    private void initAdMob() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6263632629106733/9710395681");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else {
            Toast.makeText(getBaseContext(), R.string.txtrate6,
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}