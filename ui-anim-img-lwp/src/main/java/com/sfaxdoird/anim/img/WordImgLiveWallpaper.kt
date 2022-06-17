package com.sfaxdoird.anim.img

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.sfaxdoird.anim.img.Utils.getTemporaryDirWithFolder
import com.sfaxdroid.base.SharedPrefsUtils
import com.sfaxdroid.base.utils.BitmapUtils
import java.io.File

internal class WordImgLiveWallpaper : WallpaperService() {

    override fun onCreateEngine(): Engine {
        val pref = SharedPrefsUtils(this)
        val color = pref.getSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, -4522170)
        val zipFolder = com.sfaxdroid.base.Constants.ZIP_FOLDER_NAME
        return ImgWordEngine(
            color,
            getTemporaryDirWithFolder(
                this,
                folder = Constants.KEY_LWP_FOLDER_CONTAINER,
                appName = getString(R.string.app_namenospace)
            ),
            com.sfaxdroid.base.Constants.PNG_BACKFROUND_FILE_NAME, zipFolder
        )
    }

    private inner class ImgWordEngine constructor(
        private val color: Int,
        private val tempDir: File,
        private val backgroundFileName: String,
        private val zipFolder: String
    ) : Engine() {
        private val mHolder = surfaceHolder
        private val mHandler = Handler()
        private val options = BitmapFactory.Options()
        private var screenHeight = 0
        private var screenWidth = 0
        private var background: Bitmap? = null
        private var wordBmp: Bitmap?
        private var isWallpaperVisible = false
        private val drawPattern = Runnable { drawFrame() }
        private val paintOption: Paint

        init {
            wordBmp = BitmapUtils.changeImageColor(
                BitmapFactory.decodeFile(
                    "$tempDir/$zipFolder/$prefix$currentPhoto.png",
                    options
                ),
                color
            )
            paintOption = Paint().apply {
                color = -0x1
                isAntiAlias = true
                strokeWidth = 2f
                strokeCap = Paint.Cap.ROUND
                style = Paint.Style.STROKE
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)
        }

        override fun onDestroy() {
            super.onDestroy()
            mHandler.removeCallbacks(drawPattern)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            isWallpaperVisible = visible
            if (visible) {
                drawFrame()
            } else {
                mHandler.removeCallbacks(drawPattern)
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            BitmapFactory.decodeFile(
                "$tempDir/$backgroundFileName",
                options
            )?.let {
                background = Bitmap.createScaledBitmap(it, width, height, true)
                it.recycle()
                background?.let {
                    drawFrame()
                }
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)

            isWallpaperVisible = false
            mHandler.removeCallbacks(drawPattern)

            background?.apply {
                recycle()
            }

            wordBmp?.apply {
                recycle()
            }
        }

        override fun onOffsetsChanged(
            xOffset: Float,
            yOffset: Float,
            xStep: Float,
            yStep: Float,
            xPixels: Int,
            yPixels: Int
        ) {
            drawFrame()
        }

        private val prefix: String
            get() = if (currentPhoto < 10) {
                "i_000"
            } else {
                "i_00"
            }

        override fun onTouchEvent(event: MotionEvent) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                com.sfaxdroid.base.Constants.nbIncrementationAfterChange =
                    com.sfaxdroid.base.Constants.nbIncrementationAfterChange + 1
                if (screenWidth > 0 && com.sfaxdroid.base.Constants.ifBackgroundChanged && com.sfaxdroid.base.Constants.nbIncrementationAfterChange == 5) {
                    com.sfaxdroid.base.Constants.nbIncrementationAfterChange = 0
                    com.sfaxdroid.base.Constants.ifBackgroundChanged = false
                    background = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeFile(
                            "$tempDir/$backgroundFileName",
                            options
                        ),
                        screenWidth, screenHeight, true
                    )
                }
                // TODO maybe change color
                wordBmp = BitmapUtils.changeImageColor(
                    BitmapFactory.decodeFile(
                        "$tempDir/$zipFolder/$prefix$currentPhoto.png",
                        options
                    ),
                    color, screenWidth / 2, screenHeight / 2
                )
                if (currentPhoto == nbMaxPhoto) {
                    currentPhoto = 0
                }
                currentPhoto++
            }
            super.onTouchEvent(event)
        }

        fun drawFrame() {
            var canvas: Canvas? = null
            try {
                canvas = mHolder.lockCanvas()
                canvas?.let { drawPattern(it) }
            } finally {
                canvas?.let { mHolder.unlockCanvasAndPost(it) }
            }
            mHandler.removeCallbacks(drawPattern)
            if (isWallpaperVisible) {
                mHandler.postDelayed(drawPattern, 1000 / 25.toLong())
            }
        }

        fun drawPattern(canvas: Canvas) {
            canvas.apply {
                save()
                drawColor(-0x1000000)

                background?.let {
                    if (it.width != 0)
                        drawBitmap(it, 0f, 0f, null)
                }

                screenWidth = width
                screenHeight = height

                wordBmp?.let {
                    val debut = screenWidth / 2 - it.width / 2
                    val fin = screenHeight / 2 - it.height / 2
                    if (it.width != 0)
                        drawBitmap(it, debut.toFloat(), fin.toFloat(), paintOption)
                }

                restore()
            }
        }
    }

    companion object {
        var currentPhoto = 1
        var nbMaxPhoto = 58
    }
}
