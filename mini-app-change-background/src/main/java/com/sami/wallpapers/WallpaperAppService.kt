package com.sami.wallpapers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.view.animation.DecelerateInterpolator
import com.sfaxdroid.mini.base.Constans
import java.util.*

class WallpaperAppService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        val sharedPrefs =
            getSharedPreferences(Constans.PREF_NAME, Context.MODE_PRIVATE)
        val qualityPref = sharedPrefs.getString(Constants.PREF_KEY_QUALITY, "none")
        val speedPref = sharedPrefs.getString(Constants.PREF_KEY_SPEED, "")
        return WallpaperEngine(this, qualityPref, speedPref)
    }

    inner class WallpaperEngine(
        private var context: Context,
        var qualityPref: String?,
        var speedPref: String?
    ) :
        Engine() {

        private val frameMaxDigit = if (BuildConfig.FLAVOR == "big")
            99 else 70
        private var currentPosition = 0
        private var speed = 10000
        private val handler = Handler()
        private val transformationMatrix = Matrix()
        private val bitmapOptions = BitmapFactory.Options()
        private val decelerateInterpolator =
            DecelerateInterpolator(1f)
        private var mScreenWidth = 0
        private var mScreenHeight = 0
        private var mTickCount = 0
        private val mDrawRunnable = Runnable { draw() }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)

            if (qualityPref.equals("quality1", ignoreCase = true)) {
                bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
            } else {
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            setSpeedFromPref()
            if (visible) {
                draw()
            } else {
                handler.removeCallbacks(mDrawRunnable)
            }
        }

        private fun setSpeedFromPref() {

            when (speedPref) {
                Constants.PREF_VALUE_SPEED_1 -> speed = 300000
                Constants.PREF_VALUE_SPEED_2 -> speed = 20000
                Constants.PREF_VALUE_SPEED_3 -> speed = 10000
                Constants.PREF_VALUE_SPEED_4 -> speed = 8000
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder, format: Int,
            width: Int, height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            mScreenWidth = width
            mScreenHeight = height
        }

        private fun draw() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                canvas?.let { drawWallpaper(it) }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
            handler.removeCallbacks(mDrawRunnable)
            if (mTickCount == 0) {
                startWithTime(speed.toLong())
            } else {
                startWithTime(
                    (speed * decelerateInterpolator
                        .getInterpolation(
                            mTickCount
                                    / frameMaxDigit.toFloat()
                        )).toLong()
                )
            }
        }

        private fun startWithTime(time: Long) {
            handler.postDelayed(mDrawRunnable, time)
        }

        private fun drawWallpaper(canvas: Canvas) {
            if (mTickCount > frameMaxDigit) {
                mTickCount = 0
            }
            currentPosition++
            if (currentPosition >= frameMaxDigit) {
                currentPosition = 0
            }
            BitmapFactory.decodeResource(
                context.resources,
                context.resources.getIdentifier(
                    "img_"
                            + String.format(
                        "%05d",
                        currentPosition % frameMaxDigit,
                        Locale.US
                    ), "drawable",
                    context.packageName
                ),
                bitmapOptions
            )?.let {
                canvas.drawBitmap(
                    Bitmap.createScaledBitmap(
                        it, mScreenWidth,
                        mScreenHeight, true
                    ), transformationMatrix, null
                )
                it.recycle()
            }
            mTickCount++
        }

    }
}