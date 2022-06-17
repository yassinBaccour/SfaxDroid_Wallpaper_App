package com.sfaxdroid.base

import android.app.Activity
import android.content.Context

interface Ads {
    fun showInterstitial(activity: Activity)
    fun loadInterstitial(context: Context)
}