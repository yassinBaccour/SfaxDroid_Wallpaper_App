package com.yassin.wallpaper.home

import SfaxDroidTheme
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.base.extension.checkAppPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.sfaxdroid.base.Ads
import com.sfaxdroid.base.PrivacyManager
import com.sfaxdroid.base.SfaxDroidRating
import com.sfaxdroid.data.entity.AppName
import javax.inject.Named

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @Inject
    lateinit var ads: Ads

    @Inject
    lateinit var privacyManager: PrivacyManager

    @Inject
    lateinit var rating: SfaxDroidRating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SfaxDroidTheme {
                MainHome(AppName.AccountOne) {
                    windowsMode(it)
                }
            }
        }
        privacyManager.loadConsent(this)
        ads.loadInterstitial(this)
        this.checkAppPermission()
    }

    private fun windowsMode(destFragment: String) {
        if (destFragment == "Wallpaper") {
            ads.incrementNbWallPaperLoaded()
            ads.showInterstitial(this)
            rating.manageNbRunApp(this)
        }
        if (destFragment == "DetailsFragment") {
        } else {
        }
    }


    companion object {
        var nbOpenAds = 0
    }
}
