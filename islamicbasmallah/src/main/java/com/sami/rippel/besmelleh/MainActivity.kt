package com.sami.rippel.besmelleh

import android.os.Bundle
import android.view.View
import com.sfaxdroid.base.BaseActivity
import com.sfaxdroid.base.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSetWallpaper.setOnClickListener {
            Utils.openLiveWallpaper<WallpaperEngine>(this)
        }
        findViewById<View>(R.id.buttonRating).setOnClickListener {
            Utils.ratingApplication(this)
        }
        findViewById<View>(R.id.imageViewPub).setOnClickListener {
            Utils.openPub(this)
        }
    }
}