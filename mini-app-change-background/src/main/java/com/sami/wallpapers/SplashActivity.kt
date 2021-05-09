package com.sami.wallpapers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

private var mInterstitialAd: InterstitialAd? = null
private var canShowAds = (BuildConfig.FLAVOR == "big")

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initAdMob()
        if (canShowAds && mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        }
    }

    private fun initAdMob() {
        MobileAds.initialize(
            this
        ) { }
        InterstitialAd.load(
            this,
            Constants.AD_MOB,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    startActivity()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            }
        )
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                startActivity()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                startActivity()
            }

            override fun onAdShowedFullScreenContent() {
                mInterstitialAd = null
            }
        }
    }

    private fun startActivity() {
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
        finish()
    }
}
