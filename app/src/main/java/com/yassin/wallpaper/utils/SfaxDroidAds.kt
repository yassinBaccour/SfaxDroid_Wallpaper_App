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
import com.yassin.wallpaper.home.HomeActivity
import javax.inject.Inject
import javax.inject.Named

class SfaxDroidAds @Inject constructor(
    @Named("interstitial-key") var interstitialKey: String
) :
    Ads {

    private var mInterstitialAd: InterstitialAd? = null
    private var wallpaperLoaded = 0
    private var nbShowedPerSession = 0
    private var isFirstAdsLoaded = false

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
        if (getNbWallPaperLoaded() >= 2) {
            nbShowedPerSession++
            isFirstAdsLoaded = true
            if (!isFirstAdsLoaded) {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(activity)
                    loadInterstitial(activity.baseContext)
                }
            } else {
                HomeActivity.nbOpenAds++
                if (HomeActivity.nbOpenAds == 4 && nbShowedPerSession < 3) {
                    HomeActivity.nbOpenAds = 0
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(activity)
                        loadInterstitial(activity.baseContext)
                    }
                }
            }
        }
    }

    override fun incrementNbWallPaperLoaded() {
        wallpaperLoaded++
    }

    override fun getNbWallPaperLoaded() = wallpaperLoaded
}