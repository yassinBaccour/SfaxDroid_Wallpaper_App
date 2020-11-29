package com.yassin.sfax.sobhanallahlwp;

import java.util.Locale;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {
    ;
    String packageManager = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ImageView imageViewPub = findViewById(R.id.imageViewPub);

        boolean isAr = Locale.getDefault().getLanguage().contentEquals("ar");
        if (isAr) {
            imageViewPub.setImageDrawable(getResources().getDrawable(R.drawable.ic_pub2));
            packageManager = "market://details?id=com.islama.worldstickers";
        } else {
            imageViewPub.setImageDrawable(getResources().getDrawable(R.drawable.ic_pub));
            packageManager = "market://details?id=com.sami.rippel.allah";
        }

        imageViewPub.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri
                        .parse(packageManager));
                startActivity(intent);
            }
        });


        findViewById(R.id.buttonSetWallpaper).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    Intent intent = new Intent(
                            WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(
                            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            new ComponentName(getApplicationContext(),
                                    LiveWallpaper.class));
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

        findViewById(R.id.btRating).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri
                        .parse("market://details?id=com.yassin.sfax.sobhanallahlwp"));
                startActivity(intent);
            }
        });

        findViewById(R.id.btotherapp).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:SFAXDROID"));
                startActivity(intent);
            }
        });
    }

}