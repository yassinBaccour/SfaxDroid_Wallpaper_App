package com.yassin.wallpaper.feature.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.base.SimpleActivity
import com.sfaxdroid.base.extension.checkAppPermission
import com.yassin.wallpaper.R
import com.yassin.wallpaper.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeActivityNavBar : SimpleActivity() {

    private var mToolbar: Toolbar? = null
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var preferencesManager: PreferencesManager

    @Inject
    @Named("intertitial-key")
    lateinit var intertitialKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this, "ccc")
        setupAds()
        initView()
        setupToolBar()
        initRatingApp()
        manageNbRunApp()
        this.checkAppPermission()
        loadFragment()
    }

    private fun loadFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private fun initView() {
        mToolbar = findViewById(R.id.toolbar)
    }

    private fun setupAds() {
        MobileAds.initialize(this)
        InterstitialAd.load(
            this,
            intertitialKey,
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

    override val layout: Int
        get() = R.layout.activity_home_nav

    override fun onResume() {
        super.onResume()
        showFirstTimeAndOneTimeAds()
        showTimedAdsWhenIOpenPicture()
    }

    fun initRatingApp() {
    }

    private fun manageNbRunApp() {

        var nbRun = preferencesManager["NbRun", 0]
        when {
            preferencesManager["IsSecondRun", "null"] == "null" -> {

                preferencesManager["IsSecondRun"] = "Second"
                nbRun += 1
                preferencesManager["NbRun"] = nbRun
            }
            nbRun == 3 -> {
                preferencesManager["NbRun"] = 0
            }
            else -> {
                nbRun += 1
                preferencesManager["NbRun"] = nbRun
            }
        }
    }

    private fun setupToolBar() {
        setSupportActionBar(mToolbar)
    }

    private fun rateApplication() {
        if (preferencesManager[
            Constants.RATING_MESSAGE,
            Constants.RATING_YES
        ]
            == Constants.RATING_YES
        ) {
        }
    }

    private fun showFirstTimeAndOneTimeAds() {
        if (isAdsShow) {
            isAdsShow = false
            showInterstitial()
        }
    }

    private fun showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        }
    }

    private fun showTimedAdsWhenIOpenPicture() {
        if (nbOpenAds == 4) {
            nbOpenAds = 0
            showInterstitial()
        }
        if (nbOpenAds == 7) {
            rateApplication()
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun initEventAndData() {}

    companion object {
        var isAdsShow = false
        var nbOpenAds = 0
    }
}
