package com.sfaxdroid.sky

import android.content.Context
import android.preference.PreferenceManager
import org.rajawali3d.renderer.ISurfaceRenderer
import org.rajawali3d.view.ISurface
import org.rajawali3d.wallpaper.Wallpaper
import java.lang.Exception
import java.lang.reflect.InvocationTargetException

class SkyLiveWallpaper : Wallpaper() {

    override fun onCreateEngine(): Engine {
        val mRenderer = try {
            val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val rendererClass = Class.forName(
                mSharedPreferences.getString(
                    "renderer_class",
                    WallpaperRenderer::class.java.canonicalName
                )!!
            )
            rendererClass.getConstructor(Context::class.java)
                .newInstance(this) as ISurfaceRenderer
        } catch (e: Exception) {
            WallpaperRenderer(this)
        }

        return WallpaperEngine(
            baseContext,
            mRenderer,
            ISurface.ANTI_ALIASING_CONFIG.NONE
        )
    }
}