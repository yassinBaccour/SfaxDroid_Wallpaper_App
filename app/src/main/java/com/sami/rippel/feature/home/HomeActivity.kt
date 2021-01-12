package com.sami.rippel.feature.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sami.rippel.allah.R
import com.sami.rippel.core.setupWithNavController
import com.sfaxdroid.base.PreferencesManager
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private var currentNavController: LiveData<NavController>? = null

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupToolBar()
        if (savedInstanceState == null) {
            initView()
        }
        preferencesManager = PreferencesManager(this, com.sfaxdroid.base.Constants.PREFERENCES_NAME)
        setupAds()

        initRatingApp()
        manageNbRunApp()
        checkPermission()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        initView()
    }

    private fun initView() {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val navGraphIds = listOf(
            R.navigation.lwp_nav_graph,
            R.navigation.wallpaper_nav_graph,
            R.navigation.category_nav_graph
        )
        val controller = bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun setupAds() {
        MobileAds.initialize(this)
        InterstitialAd(this).apply {
            adUnitId = getString(R.string.intertitial)
            loadAd(AdRequest.Builder().build())
            adListener = object : AdListener() {
                override fun onAdClosed() {
                    loadAd(AdRequest.Builder().build())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showFirstTimeAndOneTimeAds()
        showTimedAdsWhenIOpenPicture()
    }

    private fun initRatingApp() {
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

    @SuppressLint("CheckResult")
    fun checkPermission() {
        RxPermissions(this).request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS
        )?.subscribe { }
    }

    private fun setupToolBar() {
    }


    private fun showFirstTimeAndOneTimeAds() {
        if (isAdsShow) {
            isAdsShow = false
            showInterstitial()
        }
    }

    private fun showInterstitial() {
        if (mInterstitialAd?.isLoaded == true) {
            mInterstitialAd?.show()
        }
    }

    private fun showTimedAdsWhenIOpenPicture() {
        if (nbOpenAds == 4) {
            nbOpenAds = 0
            showInterstitial()
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    companion object {

        @JvmField
        var isAdsShow = false

        @JvmField
        var nbOpenAds = 0
        private var back_pressed: Long = 0
    }
}