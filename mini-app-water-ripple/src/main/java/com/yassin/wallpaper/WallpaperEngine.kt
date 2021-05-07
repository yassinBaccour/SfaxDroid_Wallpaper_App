package com.yassin.wallpaper

import com.sfaxdroid.engine.BaseWallpaperEngine

class WallpaperEngine : BaseWallpaperEngine() {

    override fun getWallpaperOne(): Int {
        return R.drawable.ic_wallpaper_one
    }

    override fun getWallpaperTwo(): Int {
        return R.drawable.ic_wallpaper_two
    }

    override fun getWallpaperThree(): Int {
        return R.drawable.ic_wallpaper_three
    }
}
