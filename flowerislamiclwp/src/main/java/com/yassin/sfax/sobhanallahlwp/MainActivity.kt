package com.yassin.sfax.sobhanallahlwp

import android.os.Bundle
import com.sfaxdroid.base.BaseActivity
import com.sfaxdroid.base.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageViewPub.setOnClickListener {
            Utils.openPub(this)
        }
        buttonSetWallpaper.setOnClickListener {
            Utils.openLiveWallpaper<LiveWallpaper>(this)
        }
        btRating.setOnClickListener {
            Utils.ratingApplication(this)
        }
    }
}