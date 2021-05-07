package com.sfaxdroid.sky

import org.rajawali3d.view.ISurface
import org.rajawali3d.wallpaper.Wallpaper

class SkyLiveWallpaper : Wallpaper() {

    override fun onCreateEngine(): Engine {
        return WallpaperEngine(
            baseContext,
            WallpaperRenderer(this, R.drawable.night),
            ISurface.ANTI_ALIASING_CONFIG.NONE
        )
    }
}
