package com.sfaxdroid.wallpaper

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.sfaxdroid.sky.SkyBoxLiveWallpaper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var manager: ReviewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen(rateApp = { reviewApp() }) {
                openLiveWallpaper<SkyBoxLiveWallpaper>(this)
            }
        }
    }

    private fun reviewApp() {
        manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener {
            val reviewFlow = manager.launchReviewFlow(this, it.result)
            reviewFlow.addOnCompleteListener {}
        }
    }

    inline fun <reified T : Any> openLiveWallpaper(context: Context) {
        try {
            context.startActivity(
                Intent(
                    WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                ).apply {
                    putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(
                            context,
                            T::class.java
                        )
                    )
                }
            )
        } catch (_: Exception) {
            context.startActivity(
                Intent().apply {
                    action = WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER
                }
            )
        }
    }

}