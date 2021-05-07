package com.sfaxdroid.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * Created by souna on 12/17/2017.
 */
object BitmapUtils {

    fun changeImageColor(
        sourceBitmap: Bitmap,
        color: Int,
        width: Int = 0,
        height: Int = 0
    ): Bitmap? {
        return try {
            val resultBitmap: Bitmap = if (width != 0) {
                Bitmap.createScaledBitmap(
                    sourceBitmap, width, height,
                    false
                )
            } else {
                Bitmap.createBitmap(
                    sourceBitmap, 0, 0,
                    sourceBitmap.width - 1, sourceBitmap.height - 1
                )
            }
            val canvas = Canvas(resultBitmap)
            canvas.drawBitmap(resultBitmap, 0f, 0f, Paint().apply {
                colorFilter = LightingColorFilter(color, 1)
            })
            resultBitmap
        } catch (e: Exception) {
            null
        }
    }

    private fun covertBitmapToDrawable(context: Context, bitmap: Bitmap?): Drawable {
        return BitmapDrawable(context.resources, bitmap)
    }

    fun covertToDrawableWithColorChange(
        context: Context,
        selectedColor: Int,
        drawable: Drawable
    ): Drawable {
        return covertBitmapToDrawable(
            context,
            changeImageColor(
                convertDrawableToBitmap(
                    drawable
                ), selectedColor
            )
        )
    }

    private fun convertDrawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
