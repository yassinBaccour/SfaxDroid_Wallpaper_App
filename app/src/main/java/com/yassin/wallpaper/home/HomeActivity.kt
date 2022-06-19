package com.yassin.wallpaper.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.base.extension.checkAppPermission
import com.yassin.wallpaper.R
import com.yassin.wallpaper.setupWithNavController
import com.yassin.wallpaper.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

import com.sfaxdroid.base.Ads
import com.sfaxdroid.base.PrivacyManager

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private var nbShowedPerSession = 0
    private var isFirstAdsLoaded = false
    private lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @Inject
    lateinit var ads: Ads

    @Inject
    lateinit var privacyManager: PrivacyManager

    @Inject
    @Named("interstitial-key")
    lateinit var interstitialKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolBar()
        if (savedInstanceState == null) {
            initView()
        }
        privacyManager.loadConsent(this)
        ads.loadInterstitial(this)
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

    var wallpaperLoaded = 0

    private fun windowsMode(destFragment: String) {
        if (destFragment == "Wallpaper") {
            wallpaperLoaded++
            if (wallpaperLoaded >= 2) {
                if (!isFirstAdsLoaded) {
                    showInterstitialAds()
                } else {
                    manageNbRunApp()
                    showInterstitial()
                }
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

    private fun ratingApp() {
        val reviewManager = ReviewManagerFactory.create(this@HomeActivity)
        reviewManager.requestReviewFlow()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reviewManager.launchReviewFlow(this@HomeActivity, task.result)
                        .addOnCompleteListener {
                        }
                }
            }
    }

    private fun manageNbRunApp() {
        var nbRun = preferencesManager["NbRun", 0]
        when (nbRun) {
            0, 10, 20 -> {
                ratingApp()
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
        ads.showInterstitial(this)
        nbShowedPerSession++
        isFirstAdsLoaded = true
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
