package com.yassin.wallpaper.feature.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManagerFactory
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.base.extension.checkAppPermission
import com.yassin.wallpaper.R
import com.yassin.wallpaper.core.setupWithNavController
import com.yassin.wallpaper.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private var currentNavController: LiveData<NavController>? = null
    private var nbShowedPerSession = 0
    private var isFirstAdsLoaded = false
    private lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @Inject
    @Named("intertitial-key")
    lateinit var intertitialKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolBar()
        if (savedInstanceState == null) {
            initView()
        }
        setupAds()
        manageNbRunApp()
        this.checkAppPermission()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        initView()
    }

    private fun initView() {
        val navGraphIds = listOf(
            R.navigation.wallpaper_nav_graph,
            R.navigation.lwp_nav_graph,
            R.navigation.category_nav_graph
        )
        val controller = binding.bottomNavView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = binding.navHostFragment.id,
            intent = intent
        )
        currentNavController = controller
        currentNavController?.observe(this) { navController ->
            navController.addOnDestinationChangedListener { _, dest, _ ->
                windowsMode(dest.label.toString())
            }
        }
        currentNavController = controller
    }

    private fun windowsMode(destFragment: String) {
        if (destFragment == "Wallpaper") {
            if (!isFirstAdsLoaded) {
                showInterstitialAds()
            } else {
                if (preferencesManager["ratingAppAtFirstInstall", true]) {
                    ratingApp()
                }
                showInterstitial()
            }
        }
        if (destFragment == "DetailsFragment") {
            hideSystemUI()
        } else {
            showSystemUI()
        }
    }

    private fun hideSystemUI() {
        binding.bottomNavView.visibility = View.GONE
    }

    private fun showSystemUI() {
        binding.bottomNavView.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
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

    private fun ratingApp() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.rating_app_title))
            setMessage(getString(R.string.rating_app_description))
            setPositiveButton(
                getString(R.string.rating_app_yes_btn)
            ) { _, _ ->
                val reviewManager = ReviewManagerFactory.create(this@HomeActivity)
                reviewManager.requestReviewFlow()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reviewManager.launchReviewFlow(this@HomeActivity, task.result)
                                .addOnCompleteListener {
                                    preferencesManager["NbRun"] = 100
                                    preferencesManager["ratingAppAtFirstInstall"] = false
                                }
                        }
                    }
            }
            setNegativeButton(
                getString(R.string.rating_app_later)
            ) { _, _ ->
                preferencesManager["NbRun"] = 0
                preferencesManager["ratingAppAtFirstInstall"] = false
            }
            setNeutralButton(
                getString(R.string.rating_app_never)
            ) { _, _ ->
                preferencesManager["NbRun"] = 100
                preferencesManager["ratingAppAtFirstInstall"] = false
            }
        }.create().show()
    }

    private fun manageNbRunApp() {
        var nbRun = preferencesManager["NbRun", 0]
        when (nbRun) {
            3 -> {
                ratingApp()
                preferencesManager["NbRun"] = 100
            }
            else -> {
                nbRun += 1
                preferencesManager["NbRun"] = nbRun
            }
        }
    }

    private fun setupToolBar() {
    }

    private fun showInterstitial() {
        nbOpenAds++
        if (nbOpenAds == 4 && nbShowedPerSession < 3) {
            nbOpenAds = 0
            showInterstitialAds()
        }
    }

    private fun showInterstitialAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
            nbShowedPerSession++
            isFirstAdsLoaded = true
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            this.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
        }
        return super.onOptionsItemSelected(menuItem)
    }

    companion object {
        var nbOpenAds = 0
    }
}
