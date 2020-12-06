package com.sami.wallpapers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Handler
import android.preference.PreferenceManager
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.view.animation.DecelerateInterpolator
import java.util.*

class WallpaperAppService : WallpaperService() {
    override fun onCreateEngine(): Engine {

        val sharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(this)
        val qualityPref = sharedPrefs.getString("prefquality", "none")
        val speedPref = sharedPrefs.getString("prefSyncFrequency", "")
        return WallpaperEngine(this, qualityPref, speedPref)
    }

    inner class WallpaperEngine(
        var context: Context,
        var qualityPref: String?,
        var speedPref: String?
    ) :
        Engine() {

        private val frameMaxDigit = 99
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
                "speed1" -> speed = 300000
                "speed2" -> speed = 20000
                "speed3" -> speed = 10000
                "speed4" -> speed = 8000
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
            val bitmap = BitmapFactory.decodeResource(
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
            )
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap, mScreenWidth,
                mScreenHeight, true
            )
            canvas.drawBitmap(scaledBitmap, transformationMatrix, null)
            bitmap.recycle()
            mTickCount++
        }

    }
}