package com.yassin.wallpaper.utils

import android.app.Activity
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.sfaxdroid.base.PrivacyManager
import javax.inject.Inject

class SfaxDroidPrivacy @Inject constructor() : PrivacyManager {

    private var consentInformation: ConsentInformation? = null
    private var consentForm: ConsentForm? = null

    override fun loadConsent(activity: Activity) {
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()
        consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation?.requestConsentInfoUpdate(
            activity,
            params,
            {
                if (consentInformation?.isConsentFormAvailable == true) {
                    loadForm(activity);
                }
            },
            {
            })
    }

    private fun loadForm(activity: Activity) {
        UserMessagingPlatform.loadConsentForm(
            activity,
            { consentForm ->
                this.consentForm = consentForm
                if (consentInformation?.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                    consentForm.show(activity) {
                        loadForm(activity)
                    }
                }
            }
        ) {
        }
    }
}