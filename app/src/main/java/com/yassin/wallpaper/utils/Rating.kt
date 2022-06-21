package com.yassin.wallpaper.utils

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory
import com.sfaxdroid.base.PreferencesManager
import com.sfaxdroid.base.SfaxDroidRating
import javax.inject.Inject

class Rating @Inject constructor(var preferencesManager: PreferencesManager) : SfaxDroidRating {

    override fun ratingApp(activity: Activity) {
        val reviewManager = ReviewManagerFactory.create(activity)
        reviewManager.requestReviewFlow()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reviewManager.launchReviewFlow(activity, task.result)
                        .addOnCompleteListener {
                        }
                }
            }
    }

    override fun manageNbRunApp(activity: Activity) {
        var nbRun = preferencesManager["NbRun", 0]
        when (nbRun) {
            0, 10, 20 -> {
                ratingApp(activity)
            }
            else -> {
                nbRun += 1
                preferencesManager["NbRun"] = nbRun
            }
        }
    }
}