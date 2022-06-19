package com.sfaxdroid.base

import android.app.Activity

interface PrivacyManager {
    fun loadConsent(activity: Activity)
}