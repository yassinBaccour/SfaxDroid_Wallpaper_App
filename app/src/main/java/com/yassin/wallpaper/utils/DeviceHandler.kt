package com.yassin.wallpaper.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.sfaxdroid.base.DeviceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceHandler @Inject constructor(@ApplicationContext private val context: Context) :
    DeviceManager {

    override fun isSmallScreen(): Boolean {
        return getScreenHeightPixels(context) < 820 &&
            getScreenWidthPixels(context) < 500
    }

    override fun getScreenHeightPixels(): Int {
        return getScreenHeightPixels(context)
    }

    override fun getScreenWidthPixels(): Int {
        return getScreenWidthPixels(context)
    }

    private fun getScreenHeightPixels(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }

    private fun getScreenWidthPixels(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val mWindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        mWindowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }
}
