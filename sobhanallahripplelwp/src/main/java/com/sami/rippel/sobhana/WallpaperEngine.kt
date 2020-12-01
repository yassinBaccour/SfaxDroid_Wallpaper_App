package com.sami.rippel.sobhana

import com.sfaxdroid.engine.BaseWallpaperEngine

class WallpaperEngine : BaseWallpaperEngine() {

    override fun getWallpaperOne(): Int {
        return R.drawable.ic_wallpaper_one
    }

    override fun getWallpaperTwo(): Int {
        return 0
    }

    override fun getWallpaperThree(): Int {
        return 0
    }

}