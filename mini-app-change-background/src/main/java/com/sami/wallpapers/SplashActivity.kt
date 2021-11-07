package com.sami.wallpapers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class SplashActivity : ComponentActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private var canShowAds = (BuildConfig.FLAVOR == "big")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SfaxDroidThemes {
                SplashScreen()
            }
        }
        initAdMob()
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
                    interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
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
                    if (canShowAds && mInterstitialAd != null) {
                        mInterstitialAd?.show(this@SplashActivity)
                    }
                }
            }
        )
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
