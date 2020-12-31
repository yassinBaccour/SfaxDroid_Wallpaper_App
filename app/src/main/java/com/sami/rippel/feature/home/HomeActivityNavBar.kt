package com.sami.rippel.feature.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.kobakei.ratethisapp.RateThisApp
import com.sami.rippel.allah.R
import com.sami.rippel.feature.other.PrivacyActivity
import com.sami.rippel.feature.home.AllInOneFragment.Companion.newInstance
import com.sami.rippel.utils.Constants
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.base.SimpleActivity
import com.sfaxdroid.bases.DeviceListner
import com.tbruyelle.rxpermissions2.RxPermissions

class HomeActivityNavBar : SimpleActivity(), DeviceListner {

    private var mToolbar: Toolbar? = null
    private var mPrivacy: ImageView? = null
    private var rxPermissions: RxPermissions? = null
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this, "ccc")
        rxPermissions = RxPermissions(this)
        setupAds()
        initView()
        setupToolBar()
        initRatingApp()
        manageNbRunApp()
        checkPermission()
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
        mPrivacy?.setOnClickListener { x: View? ->
            val intent = Intent(
                this,
                PrivacyActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun setupAds() {
        MobileAds.initialize(this) { initializationStatus: InitializationStatus? -> }
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
        val config = RateThisApp.Config(3, 5)
        config.setTitle(R.string.rating_app_title)
        config.setMessage(R.string.rating_app_description)
        config.setYesButtonText(R.string.rating_app_yes_btn)
        config.setNoButtonText(R.string.rating_app_never)
        config.setCancelButtonText(R.string.rating_app_later)
        RateThisApp.setCallback(object : RateThisApp.Callback {
            override fun onYesClicked() {
                RateThisApp.stopRateDialog(this@HomeActivityNavBar)
                preferencesManager[Constants.RATING_MESSAGE] =
                    Constants.RATING_NON
            }

            override fun onNoClicked() {
                preferencesManager[Constants.RATING_MESSAGE] =
                    Constants.RATING_NON
            }

            override fun onCancelClicked() {}
        })
        RateThisApp.init(config)
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

    private fun checkPermission() {
        rxPermissions?.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS
        )?.subscribe { granted: Boolean? -> }
    }

    private fun setupToolBar() {
        setSupportActionBar(mToolbar)
    }

    private fun rateApplication() {
        if (preferencesManager[Constants.RATING_MESSAGE,
                    Constants.RATING_YES]
            == Constants.RATING_YES
        ) {
            RateThisApp.showRateDialog(this)
        }
    }

    fun showFirstTimeAndOneTimeAds() {
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

    fun showTimedAdsWhenIOpenPicture() {
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
        RateThisApp.onStart(this)
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

    override fun onRequestPermissions() {
        ActivityCompat.requestPermissions(
            this@HomeActivityNavBar, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.SET_WALLPAPER_HINTS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.SET_WALLPAPER,
                Manifest.permission.RECEIVE_BOOT_COMPLETED
            ),
            REQUEST_CODE_ASK_PERMISSIONS
        )
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun initEventAndData() {}

    companion object {
        private const val REQUEST_CODE_ASK_PERMISSIONS = 123
        var isAdsShow = false
        var nbOpenAds = 0
        private var back_pressed: Long = 0
    }
}