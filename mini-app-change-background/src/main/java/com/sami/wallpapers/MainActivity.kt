package com.sami.wallpapers

import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.sfaxdroid.base.BaseMiniAppActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMiniAppActivity() {

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAdMob()
        val sharedPrefs = getSharedPreferences(
            "NAME_OF_ALLAH",
            Context.MODE_PRIVATE
        )

        val prefsEditor = sharedPrefs.edit()

        button_set_wallpaper.setOnClickListener {
            try {
                val intent = Intent(
                    WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                )
                intent.putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    ComponentName(
                        applicationContext,
                        WallpaperAppService::class.java
                    )
                )
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER
                startActivity(intent)
                Toast.makeText(
                    applicationContext, R.string.set_wallpaper_manually_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
  

        button_open_gallery.setOnClickListener {
            val intent = Intent(
                applicationContext,
                ViewWallpaperActivity::class.java
            )
            startActivity(intent)
        }
        button_privacy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.iubenda.com/privacy-policy/27298346")
            )
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
        btn_rating.setOnClickListener {
            val myAppLinkToMarket = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this@MainActivity,
                    " unable to find market app",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        when (sharedPrefs.getString("prefSyncFrequency", "none")) {
            "speed1" -> {
                radio_animation.check(R.id.speed1)
            }
            "speed2" -> {
                radio_animation.check(R.id.speed2)
            }
            "speed3" -> {
                radio_animation.check(R.id.speed3)
            }
            "speed4" -> {
                radio_animation.check(R.id.speed4)
            }
            else -> {
                radio_animation.check(R.id.speed3)
                prefsEditor.putString("prefSyncFrequency", "speed3")
                prefsEditor.apply()
            }
        }

        radio_animation.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.speed1 -> {
                    prefsEditor.putString("prefSyncFrequency", "speed1")
                    prefsEditor.commit()
                }
                R.id.speed2 -> {
                    prefsEditor.putString("prefSyncFrequency", "speed2")
                    prefsEditor.commit()
                }
                R.id.speed3 -> {
                    prefsEditor.putString("prefSyncFrequency", "speed3")
                    prefsEditor.commit()
                }
                R.id.speed4 -> {
                    prefsEditor.putString("prefSyncFrequency", "speed4")
                    prefsEditor.commit()
                }
            }
        }

        when (sharedPrefs.getString("prefQuality", "none")) {
            "quality1" -> {
                radio_quality.check(R.id.quality1)
            }
            "quality2" -> {
                radio_quality.check(R.id.quality2)
            }
            else -> {
                radio_quality.check(R.id.quality1)
                prefsEditor.putString("prefquality", "quality1")
                prefsEditor.commit()
            }
        }

        radio_quality.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.quality1 -> {
                    prefsEditor.putString("prefquality", "quality1")
                    prefsEditor.commit()
                }
                R.id.quality2 -> {
                    prefsEditor.putString("prefquality", "quality2")
                    prefsEditor.commit()
                }
            }
        }
    }

    private fun initAdMob() {
        MobileAds.initialize(
            this
        ) { }
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd?.adUnitId = "ca-app-pub-6263632629106733/9710395681"
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd?.loadAd(AdRequest.Builder().build())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mInterstitialAd?.isLoaded == true) {
            mInterstitialAd?.show()
        }
    }
}