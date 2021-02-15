package com.sami.rippel.feature.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.sami.rippel.allah.R
import com.sami.rippel.feature.other.PrivacyActivity
import com.sami.rippel.feature.home.HomeFragment.Companion.newInstance
import com.sami.rippel.utils.Constants
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.base.SimpleActivity
import com.sfaxdroid.base.extension.checkAppPermission

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
            val intent = Intent(
                this,
                PrivacyActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun setupAds() {
        MobileAds.initialize(this)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = getString(R.string.intertitial)
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
        mInterstitialAd!!.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd!!.loadAd(AdRequest.Builder().build())
            }
        }
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
        if (preferencesManager[Constants.RATING_MESSAGE,
                    Constants.RATING_YES]
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

    fun showInterstitial() {
        if (mInterstitialAd!!.isLoaded) {
            mInterstitialAd!!.show()
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

    fun checkUpdateNewWallpapers() {}
    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            rateApplication()
            Toast.makeText(
                baseContext, R.string.exit_app_message,
                Toast.LENGTH_SHORT
            ).show()
        }
        back_pressed = System.currentTimeMillis()
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
        private var back_pressed: Long = 0
    }
}