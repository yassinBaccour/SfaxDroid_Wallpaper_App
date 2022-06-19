package com.yassin.wallpaper.appInitializers

import android.app.Application
import com.sfaxdroid.base.AppInitializer
import com.yassin.wallpaper.utils.SfaxDroidAds
import javax.inject.Inject

class AdsInitializer @Inject constructor(private val ads: SfaxDroidAds) : AppInitializer {
    override fun init(application: Application) {
        ads.setupAds(application)
    }
}