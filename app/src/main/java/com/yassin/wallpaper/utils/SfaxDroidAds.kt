package com.yassin.wallpaper.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sfaxdroid.base.Ads
import javax.inject.Inject
import javax.inject.Named

class SfaxDroidAds @Inject constructor(
    @Named("interstitial-key") var interstitialKey: String
) :
    Ads {

    private var mInterstitialAd: InterstitialAd? = null

    fun setupAds(application: Application) {
        MobileAds.initialize(application)
    }

    override fun loadInterstitial(context: Context) {
        InterstitialAd.load(
            context,
            interstitialKey,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            }
        )
    }

    override fun showInterstitial(activity: Activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
            loadInterstitial(activity.baseContext)
        }
    }
}