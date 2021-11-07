package com.sami.wallpapers

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManagerFactory
import com.sami.wallpapers.ui.*

class MainActivity : ComponentActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private var canShowAds = (BuildConfig.FLAVOR == "big")
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs =
            getSharedPreferences(
                com.sfaxdroid.mini.base.BaseConstants.PREF_NAME,
                Context.MODE_PRIVATE
            )

        var speed = sharedPrefs?.getString(Constants.PREF_KEY_SPEED, "none")
        var quality = sharedPrefs?.getString(Constants.PREF_KEY_QUALITY, "none")

        setContent {
            SfaxDroidThemes {
                //HomeScreen(::rateUs, ::saveSpeed, ::saveQuality)
                ComposeNavigation()
            }
        }
    }

    @Composable
    fun ComposeNavigation() {
        val navHostController = rememberNavController()
        LaunchedEffect(Unit) {
            //initAdMob(navHostController)
        }
        NavHost(
            navController = navHostController,
            startDestination = "home_screen"
        ) {
            composable("splash_screen") {
                SplashScreen()
            }
            composable("home_screen") {
                HomeScreen(::rateUs, ::saveSpeed, ::saveQuality, navHostController)
            }
            composable("gallery_screen") {
                Gallery()
            }
            composable("detail_screen") {
                Detail()
            }
        }
    }

    private fun rateUs() {
        val reviewManager = ReviewManagerFactory.create(this)
        reviewManager.requestReviewFlow()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reviewManager.launchReviewFlow(this, task.result)
                        .addOnCompleteListener {
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

    private fun initAdMob(navHostController: NavHostController) {
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
                    navHostController.navigate("home_screen")
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                navHostController.navigate("home_screen")
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                navHostController.navigate("home_screen")
                            }

                            override fun onAdShowedFullScreenContent() {
                                mInterstitialAd = null
                            }
                        }
                    if (canShowAds && mInterstitialAd != null) {
                        mInterstitialAd?.show(this@MainActivity)
                    }
                }
            }
        )
    }

}
