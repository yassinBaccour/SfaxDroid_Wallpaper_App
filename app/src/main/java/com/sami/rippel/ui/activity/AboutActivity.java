package com.sami.rippel.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sami.rippel.allah.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iubenda.com/privacy-policy/7849148"));
        Element PrivacyElement = new Element();
        PrivacyElement.setTitle("Privacy policy");
        PrivacyElement.setIntent(browserIntent);

        Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yassinBaccour"));
        Element PrivacyElement2 = new Element();
        PrivacyElement2.setTitle("Developper GitHub");
        PrivacyElement2.setIntent(browserIntent2);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.header)
                .setDescription("Islamic Live Wallpaper")
                .addItem(new Element().setTitle("Version " + pInfo.versionName))
                .addEmail("yassin123441@gmail.com")
                .addPlayStore("com.sami.rippel.allah")
                .addInstagram("allahwallpapers")
                .addItem(PrivacyElement2)
                .addItem(PrivacyElement)
                .create();
        setContentView(aboutPage);
    }
}