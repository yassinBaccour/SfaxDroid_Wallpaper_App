package com.sfaxdroid.base.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Button
import com.sfaxdroid.base.R
import java.io.File
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Created by souna on 12/17/2017.
 */
object BitmapUtils {

    fun convertDrawableToBitmap(drawable: Drawable): Bitmap {
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

    fun covertBitmapToDrawable(context: Context, bitmap: Bitmap?): Drawable {
        return BitmapDrawable(context.resources, bitmap)
    }

    fun changeDrawableButtonColor(
        buttonColor: Button,
        context: Context,
        selectedColor: Int
    ) {
        buttonColor.setCompoundDrawablesWithIntrinsicBounds(
            null,
            covertToDrawableWithColorChange(
                context,
                selectedColor
            ), null, null
        )
    }

    fun covertToDrawableWithColorChange(
        context: Context,
        selectedColor: Int
    ): Drawable {
        return covertBitmapToDrawable(
            context,
            changeImageColor(
                convertDrawableToBitmap(
                    context.resources.getDrawable(R.mipmap.ic_palette)
                ), selectedColor
            )
        )
    }

    fun changeImageColor(sourceBitmap: Bitmap, color: Int): Bitmap? {
        return try {
            val resultBitmap = Bitmap.createBitmap(
                sourceBitmap, 0, 0,
                sourceBitmap.width - 1, sourceBitmap.height - 1
            )
            val canvas = Canvas(resultBitmap)
            canvas.drawBitmap(resultBitmap, 0f, 0f, Paint().apply {
                colorFilter = LightingColorFilter(color, 1)
            })
            resultBitmap
        } catch (e: Exception) {
            null
        }
    }
}