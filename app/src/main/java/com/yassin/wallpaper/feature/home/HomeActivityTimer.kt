package com.yassin.wallpaper.feature.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.sfaxdroid.base.extension.checkAppPermission
import com.yassin.wallpaper.R
import com.yassin.wallpaper.databinding.ActivityHomeChangedBinding
import com.yassin.wallpaper.databinding.ActivityHomeNavBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeActivityTimer : AppCompatActivity(R.layout.activity_home_changed) {

    private lateinit var binding: ActivityHomeChangedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeChangedBinding.inflate(layoutInflater)
        this.checkAppPermission()
        loadFragment()
    }

    private fun loadFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.changedFragmentContainer.id) as NavHostFragment
        navHostFragment.navController
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(menuItem)
    }

}
