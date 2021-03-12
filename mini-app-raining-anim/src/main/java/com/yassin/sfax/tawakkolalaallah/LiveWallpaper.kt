package com.yassin.sfax.tawakkolalaallah

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.sfaxdroid.mini.base.AnimatedItem
import com.sfaxdroid.mini.base.BaseConstants
import com.yassin.sfax.tawakkolalaallah.ResourceUtils.Companion.getBackground
import com.yassin.sfax.tawakkolalaallah.ResourceUtils.Companion.getRainingItem
import com.yassin.sfax.tawakkolalaallah.ResourceUtils.Companion.getTouchingItem
import java.util.*

class LiveWallpaper : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return WallpaperPatternEngine()
    }

    internal inner class WallpaperPatternEngine :
        Engine() {
        private val mHandler = Handler()
        private var mTouchX = -1f
        private var mTouchY = -1f
        private val mDrawPattern = Runnable { drawFrame() }
        private val animatedItems: ArrayList<AnimatedItem> = ArrayList()
        private var newCloudIteration = 0

        private var background = BitmapFactory.decodeResource(
            resources,
            R.drawable.wallpaper1
        )

        private var rainingItem = BitmapFactory.decodeResource(
            resources,
            R.drawable.style1_raining
        )

        private var touchingItem = BitmapFactory.decodeResource(
            resources,
            R.drawable.style1_raining
        )

        private var mVisible = false

        init {
            animatedItems.add(AnimatedItem(10, 50))
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)
        }

        override fun onDestroy() {
            super.onDestroy()
            mHandler.removeCallbacks(mDrawPattern)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            val sharedPrefs = getSharedPreferences(BaseConstants.PREF_NAME, Context.MODE_PRIVATE)
            val rainingPref = sharedPrefs.getBoolean("rainingPref", true)
            val touchingPref = sharedPrefs.getBoolean("touchingPref", true)
            val themePref = sharedPrefs.getString("themePref", "Style 1")

            background = getBackground(
                baseContext,
                themePref!!
            )
            rainingItem = getRainingItem(
                baseContext,
                themePref,
                rainingPref
            )
            touchingItem = getTouchingItem(
                baseContext,
                themePref,
                touchingPref
            )
            mVisible = visible
            if (visible) {
                drawFrame()
            } else {
                mHandler.removeCallbacks(mDrawPattern)
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder, format: Int,
            width: Int, height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            drawFrame()
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            mVisible = false
            mHandler.removeCallbacks(mDrawPattern)
        }

        override fun onOffsetsChanged(
            xOffset: Float, yOffset: Float, xStep: Float,
            yStep: Float, xPixels: Int, yPixels: Int
        ) {
            drawFrame()
        }

        override fun onTouchEvent(event: MotionEvent) {
            if (event.action == MotionEvent.ACTION_MOVE) {
                mTouchX = event.x
                mTouchY = event.y
            }
            super.onTouchEvent(event)
        }

        private fun drawFrame() {
            var canvas: Canvas? = null
            try {
                canvas = surfaceHolder.lockCanvas()
                canvas?.let { drawPattern(it) }
            } finally {
                canvas?.let { surfaceHolder.unlockCanvasAndPost(canvas) }
            }
            mHandler.removeCallbacks(mDrawPattern)
            if (mVisible) {
                mHandler.postDelayed(mDrawPattern, 1000 / 25.toLong())
            }
        }

        private fun drawPattern(canvas: Canvas) {
            canvas.save()
            drawBackground(canvas)
            drawRainingItem(canvas)
            drawTouchingItem(canvas)
        }

        private fun drawBackground(canvas: Canvas) {
            canvas.drawColor(-0x1000000)
            canvas.drawBitmap(background, 0f, 0f, null)
        }

        private fun drawRainingItem(canvas: Canvas) {
            val screenWidth = canvas.width
            if (newCloudIteration > 200 + (Math.random() * 1000 % 100).toInt()) {
                animatedItems.add(
                    AnimatedItem(
                        (Math.random() * 1000 % screenWidth).toInt(), 0
                    )
                )
                newCloudIteration = 0
            }
            newCloudIteration++
            var i = 0
            while (i < animatedItems.size) {
                canvas.drawBitmap(
                    rainingItem, animatedItems[i].x - rainingItem.width / 2.toFloat(),
                    animatedItems[i].y - rainingItem.height / 2.toFloat(), null
                )
                animatedItems[i].y++
                if (animatedItems[i].y > canvas.height) {
                    animatedItems.removeAt(i)
                    i--
                }
                i++
            }
        }

        private fun drawTouchingItem(canvas: Canvas) {
            if (mTouchX >= 0 && mTouchY >= 0) {
                canvas.drawBitmap(
                    touchingItem, mTouchX - touchingItem.width / 2, mTouchY
                            - touchingItem.height / 2, null
                )
                canvas.restore()
            }
        }

    }
}