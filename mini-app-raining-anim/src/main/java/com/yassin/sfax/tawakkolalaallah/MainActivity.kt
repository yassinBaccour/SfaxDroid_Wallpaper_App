package com.yassin.sfax.tawakkolalaallah

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.sfaxdroid.mini.base.Utils

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.buttonSetWallpaper).setOnClickListener {
            Utils.openLiveWallpaper<LiveWallpaper>(this)
        }
        findViewById<View>(R.id.buttonRating).setOnClickListener {
            Utils.ratingApplication(this)
        }
        findViewById<View>(R.id.imageViewPub).setOnClickListener {
            Utils.openPub(this)
        }
    }
}
