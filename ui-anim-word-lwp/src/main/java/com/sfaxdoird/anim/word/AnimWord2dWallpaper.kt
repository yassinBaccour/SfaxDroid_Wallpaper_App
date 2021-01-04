package com.sfaxdoird.anim.word

import android.graphics.*
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.sfaxdroid.base.SharedPrefsUtils


class AnimWord2dWallpaper : WallpaperService() {
    override fun onCreateEngine(): Engine {
        val pref = SharedPrefsUtils(this)
        val color = pref.GetSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, -4522170)
        val textSize = pref.GetSetting("2dwallpapertextsize", 1) * 20
        val fontStyle = pref.GetSetting("2dwallpaperfontstyle", 1)
        return IslamicEngine(color, textSize, fontStyle)
    }

    internal inner class IslamicEngine(private val mColor: Int, textSize: Int, fontStyle: Int) :
        Engine() {

        private var paintOption: Paint
        private val handler = Handler()
        private val mHolder = surfaceHolder
        private val filesDir = getExternalFilesDir("")
        private var screenHeight = 0
        private var screenWidth = 0
        private var background: Bitmap? = null
        private var mVisible = false
        private val drawPattern = Runnable { drawFrame() }
        private val options = BitmapFactory.Options()

        init {
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            paintOption = Paint().apply { }
            paintOption.color = mColor
            paintOption.textSize = textSize * resources.displayMetrics.density
            getTypeFace(fontStyle)?.let {
                paintOption.typeface = it
            }
        }

        private fun getTypeFace(mTypefaceNum: Int): Typeface? {
            return try {
                Typeface.createFromAsset(
                    assets,
                    "arabicfont$mTypefaceNum.ttf"
                )
            } catch (e: Exception) {
                return try {
                    Typeface.createFromAsset(
                        assets,
                        "arabicfont$mTypefaceNum.otf"
                    )
                } catch (e: Exception) {
                    Typeface.DEFAULT
                }
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacks(drawPattern)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            mVisible = visible
            if (visible) {
                drawFrame()
            } else {
                handler.removeCallbacks(drawPattern)
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder, format: Int,
            width: Int, height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)

            background?.let {
                it.recycle()
                background = null
            }

            BitmapFactory.decodeFile(
                filesDir.toString() + "/" + com.sfaxdroid.base.Constants.PNG_BACKFROUND_FILE_NAME,
                options
            )?.also {
                background = Bitmap.createScaledBitmap(it, width, height, true)
                it.recycle()
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            mVisible = false
            handler.removeCallbacks(drawPattern)
            background?.recycle()
        }

        override fun onOffsetsChanged(
            xOffset: Float, yOffset: Float, xStep: Float,
            yStep: Float, xPixels: Int, yPixels: Int
        ) {
            drawFrame()
        }

        override fun onTouchEvent(event: MotionEvent) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                com.sfaxdroid.base.Constants.nbIncrementationAfterChange =
                    com.sfaxdroid.base.Constants.nbIncrementationAfterChange + 1
                if (screenWidth > 0 && com.sfaxdroid.base.Constants.ifBackgroundChanged
                    && com.sfaxdroid.base.Constants.nbIncrementationAfterChange == 5
                ) {
                    com.sfaxdroid.base.Constants.nbIncrementationAfterChange = 0
                    com.sfaxdroid.base.Constants.ifBackgroundChanged = false
                    background = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeFile(
                            filesDir.toString() + "/"
                                    + com.sfaxdroid.base.Constants.PNG_BACKFROUND_FILE_NAME,
                            options
                        ),
                        screenWidth, screenHeight, true
                    )
                }

                //todo maybe change color
                paintOption.color = mColor
                if (currentPhoto == 98) {
                    currentPhoto = 0
                }
                currentPhoto++
            }
            super.onTouchEvent(event)
        }

        private fun drawFrame() {
            var canvas: Canvas? = null
            try {
                canvas = mHolder.lockCanvas()
                canvas?.let { drawPattern(it) }
            } finally {
                canvas?.let {
                    mHolder.unlockCanvasAndPost(canvas)
                }
            }
            handler.removeCallbacks(drawPattern)
            if (mVisible) {
                handler.postDelayed(drawPattern, 1000 / 25.toLong())
            }
        }

        private fun drawWallpaper(canvas: Canvas) {
            canvas.save()
            canvas.drawColor(-0x1000000)
            background?.let {
                canvas.drawBitmap(it, 0f, 0f, null)
            }
        }

        private fun draw2dImg(canvas: Canvas) {

            val header =
                Constants.NAME_OF_ALLAH_TAB[currentPhoto]

            val bounds = Rect()
            paintOption.getTextBounds(header, 0, header.length, bounds)
            val x = canvas.width / 2 - bounds.width() / 2
            val y = canvas.height / 2 - bounds.height() / 2

            canvas.drawText(header, x.toFloat(), y.toFloat(), paintOption)
        }

        private fun drawPattern(canvas: Canvas) {
            screenWidth = canvas.width
            screenHeight = canvas.height
            drawWallpaper(canvas)
            draw2dImg(canvas)
            canvas.restore()
        }
    }

    companion object {
        private var currentPhoto = 1
    }
}