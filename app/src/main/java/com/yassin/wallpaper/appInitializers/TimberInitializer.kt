package com.yassin.wallpaper.appInitializers

import android.app.Application
import com.sfaxdroid.app.BuildConfig
import com.sfaxdroid.base.AppInitializer
import com.yassin.wallpaper.utils.SfaxDroidLogger
import javax.inject.Inject

class TimberInitializer @Inject constructor(
    private val fmmLogger: SfaxDroidLogger
) : AppInitializer {
    override fun init(application: Application) = fmmLogger.setup(BuildConfig.DEBUG)
}
