package com.sami.rippel.feature.main.activity

import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sami.rippel.allah.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.lang.Exception

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pInfo: PackageInfo? = null
        try {
            pInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: Exception) {
        }
        setContentView(
            AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.header)
                .setDescription("Islamic Live Wallpaper")
                .addItem(Element().setTitle("Version " + pInfo?.versionName))
                .addEmail("yassin123441@gmail.com")
                .addPlayStore(packageName)
                .addInstagram("allahwallpapers")
                .addItem(Element().apply {
                    title = "Developper GitHub"
                    intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yassinBaccour"))
                })
                .addItem(Element().apply {
                    title = "Privacy policy"
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.iubenda.com/privacy-policy/7849148")
                    )
                })
                .create()
        )
    }
}