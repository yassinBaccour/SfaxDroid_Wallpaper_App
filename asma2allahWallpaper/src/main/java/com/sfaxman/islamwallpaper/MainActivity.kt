package com.sfaxman.islamwallpaper

import android.os.Bundle
import android.view.View
import com.sfaxdroid.base.BaseActivity
import com.sfaxdroid.base.Utils

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.buttonSetWallpaper).setOnClickListener {
            Utils.openLiveWallpaper<WallpaperServiceApp>(this)
        }
        findViewById<View>(R.id.imageViewPub).setOnClickListener {
            Utils.openPub(this)
        }
    }
}