package com.sami.rippel.sobhana

import android.os.Bundle
import com.sfaxdroid.base.BaseActivity
import com.sfaxdroid.base.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSetWallpaper.setOnClickListener {
            ClearCurrentWallpaper()
            Utils.openLiveWallpaper<WallpaperEngine>(this)
        }
        imageViewPub.setOnClickListener {
            Utils.openPub(this)
        }
    }
}