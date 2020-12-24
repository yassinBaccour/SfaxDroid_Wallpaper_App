package com.sami.wallpapers

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.sfaxdroid.mini.base.BaseMiniAppActivity
import com.sfaxdroid.mini.base.Constants
import com.sfaxdroid.mini.base.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMiniAppActivity() {

    private var mInterstitialAd: InterstitialAd? = null

    private var sharedPrefs: SharedPreferences? = null

    private var canShowAds = (BuildConfig.FLAVOR == "big")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

        setContentView(R.layout.activity_main)

        if (canShowAds) {
            initAdMob()
        }

        button_set_wallpaper.setOnClickListener {
            Utils.openLiveWallpaper<WallpaperAppService>(this)
        }

        button_open_gallery.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    ViewWallpaperActivity::class.java
                )
            )
        }

        button_privacy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.PRIVACY)
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
                    "Unable to find market app",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        when (sharedPrefs?.getString(Constants.PREF_KEY_SPEED, "none")) {
            Constants.PREF_VALUE_SPEED_1 -> {
                radio_animation.check(R.id.speed1)
            }
            Constants.PREF_VALUE_SPEED_2 -> {
                radio_animation.check(R.id.speed2)
            }
            Constants.PREF_VALUE_SPEED_3 -> {
                radio_animation.check(R.id.speed3)
            }
            Constants.PREF_VALUE_SPEED_4 -> {
                radio_animation.check(R.id.speed4)
            }
            else -> {
                radio_animation.check(R.id.speed3)
                saveSpeed(Constants.PREF_VALUE_SPEED_3)
            }
        }

        radio_animation.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.speed1 -> {
                    saveSpeed(Constants.PREF_VALUE_SPEED_1)
                }
                R.id.speed2 -> {
                    saveSpeed(Constants.PREF_VALUE_SPEED_2)
                }
                R.id.speed3 -> {
                    saveSpeed(Constants.PREF_VALUE_SPEED_3)
                }
                R.id.speed4 -> {
                    saveSpeed(Constants.PREF_VALUE_SPEED_4)
                }
            }
        }

        when (sharedPrefs?.getString(Constants.PREF_KEY_QUALITY, "none")) {
            Constants.PREF_VALUE_QUALITY_1 -> {
                radio_quality.check(R.id.quality1)
            }
            Constants.PREF_VALUE_QUALITY_2 -> {
                radio_quality.check(R.id.quality2)
            }
            else -> {
                radio_quality.check(R.id.quality1)
                saveQuality(Constants.PREF_VALUE_QUALITY_1)
            }
        }

        radio_quality.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.quality1 -> {
                    saveQuality(Constants.PREF_VALUE_QUALITY_1)
                }
                R.id.quality2 -> {
                    saveQuality(Constants.PREF_VALUE_QUALITY_2)
                }
            }
        }
    }

    private fun saveQuality(quality: String) {
        sharedPrefs?.edit()?.apply {
            putString(Constants.PREF_KEY_QUALITY, quality)
            apply()
        }
    }

    private fun saveSpeed(speed: String) {
        sharedPrefs?.edit()?.apply {
            putString(Constants.PREF_KEY_SPEED, speed)
            apply()
        }
    }

    private fun initAdMob() {
        MobileAds.initialize(
            this
        ) { }
        mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = Constants.AD_MOB
            loadAd(AdRequest.Builder().build())
            adListener = object : AdListener() {
                override fun onAdClosed() {
                    mInterstitialAd?.loadAd(AdRequest.Builder().build())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (canShowAds && mInterstitialAd?.isLoaded == true) {
            mInterstitialAd?.show()
        }
    }
}