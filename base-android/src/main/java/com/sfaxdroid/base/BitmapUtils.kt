package com.sfaxdroid.base

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Button
import java.io.File
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Created by souna on 12/17/2017.
 */
object BitmapUtils {
    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap? {
        return if (!source.isRecycled) {
            Bitmap.createBitmap(
                source, 0, 0, source.width,
                source.height, Matrix().apply {
                    postRotate(angle)
                }, true
            )
        } else null
    }

    fun setReducedImageSize(
        imagePath: String?,
        targetImageViewWidth: Int,
        targetImageViewHeight: Int
    ): Bitmap {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, bmOptions)
        val cameraImageWidth = bmOptions.outWidth
        val cameraImageHeight = bmOptions.outHeight
        bmOptions.inSampleSize = min(
            cameraImageWidth / targetImageViewWidth,
            cameraImageHeight / targetImageViewHeight
        )
        bmOptions.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(imagePath, bmOptions)
    }

    @JvmStatic
    fun decodeSampledBitmapFromFile(
        path: String?, reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val height = options.outHeight
        val width = options.outWidth
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        var inSampleSize = 1
        if (height > reqHeight) {
            inSampleSize = (height.toFloat() / reqHeight.toFloat()).roundToInt()
        }
        val expectedWidth = width / inSampleSize
        if (expectedWidth > reqWidth) {
            inSampleSize = (width.toFloat() / reqWidth.toFloat()).roundToInt()
        }
        options.inSampleSize = inSampleSize
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    @JvmStatic
    fun getPreview(fileName: String?): Bitmap? {
        val image = File(fileName)
        val bounds = BitmapFactory.Options()
        bounds.inJustDecodeBounds = true
        BitmapFactory.decodeFile(image.path, bounds)
        return if (bounds.outWidth == -1 || bounds.outHeight == -1) {
            null
        } else BitmapFactory.decodeFile(image.path)
    }

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
            )
            , null, null
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
                )
                , selectedColor
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