package com.sfaxdroid.base

import android.app.Activity
import android.content.Context

interface Ads {
    fun loadInterstitial(context: Context)
    fun showInterstitial(activity: Activity)
    fun incrementNbWallPaperLoaded()
    fun getNbWallPaperLoaded(): Int
}