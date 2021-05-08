package com.yassin.wallpaper.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.base.SimpleActivity
import com.sfaxdroid.base.extension.checkAppPermission
import com.yassin.wallpaper.R
import com.yassin.wallpaper.feature.home.HomeFragment.Companion.newInstance
import com.yassin.wallpaper.feature.other.PrivacyActivity
import com.yassin.wallpaper.utils.Constants

class HomeActivityNavBar : SimpleActivity() {

    private var mToolbar: Toolbar? = null
    private var mPrivacy: ImageView? = null
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var preferencesManager: PreferencesManager

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
        val mAllBackgroundFragment = newInstance("new.json", "MIXED")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.content_main, mAllBackgroundFragment, "Home.class")
        fragmentTransaction.commit()
    }

    private fun initView() {
        mToolbar = findViewById(R.id.toolbar)
        mPrivacy = findViewById(R.id.imgPrivacy)
        mPrivacy?.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PrivacyActivity::class.java
                )
            )
        }
    }

    private fun setupAds() {
        MobileAds.initialize(this)
        InterstitialAd.load(
            this,
            "",
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
