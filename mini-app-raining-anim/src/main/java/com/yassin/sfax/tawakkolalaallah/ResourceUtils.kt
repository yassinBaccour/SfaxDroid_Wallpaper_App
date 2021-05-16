package com.yassin.sfax.tawakkolalaallah

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.view.WindowManager

class ResourceUtils {
    companion object {
        fun getTouchingItem(context: Context, pref: String, Anim2: Boolean): Bitmap? {
            return if (!Anim2) {
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.none
                )
            } else when (pref) {
                "Style 1" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style1_touching
                )
                "Style 2" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style2_touching
                )
                "Style 3" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style3_touching
                )
                "Style 4" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style4_touching
                )
                "Style 5" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style5_touching
                )
                "Style 6" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style6_touching
                )
                "Style 7" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style7_touching
                )
                "Style 8" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style8_touching
                )
                "Style 9" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style9_touching
                )
                "Style 10" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style10_touching
                )
                else -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style11_touching
                )
            }
        }

        fun getRainingItem(context: Context, pref: String, Anim1: Boolean): Bitmap? {
            return if (!Anim1) {
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.none
                )
            } else when (pref) {
                "Style 1" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style1_raining
                )
                "Style 2" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style2_raining
                )
                "Style 3" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style3_raining
                )
                "Style 4" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style4_raining
                )
                "Style 5" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style5_raining
                )
                "Style 6" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style6_raining
                )
                "Style 7" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style7_raining
                )
                "Style 8" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style8_raining
                )
                "Style 9" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style9_raining
                )
                "Style 10" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style10_raining
                )
                else -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.style11_raining
                )
            }
        }

        fun getBitmap(context: Context, id: Int): Bitmap {
            val mWindowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            mWindowManager.defaultDisplay.getMetrics(displayMetrics)

            return BitmapFactory.decodeResource(
                context.resources,
                id
            ).let {
                Bitmap.createScaledBitmap(
                    it,
                    displayMetrics.widthPixels,
                    displayMetrics.heightPixels,
                    true
                )
            }
        }

        fun getBackground(context: Context, pref: String): Bitmap {

            return when (pref) {
                "Style 1" -> getBitmap(
                    context,
                    R.drawable.wallpaper1
                )
                "Style 2" -> getBitmap(
                    context,
                    R.drawable.wallpaper2
                )
                "Style 3" -> getBitmap(
                    context,
                    R.drawable.wallpaper3
                )
                "Style 4" -> getBitmap(
                    context,
                    R.drawable.wallpaper4
                )
                "Style 5" -> getBitmap(
                    context,
                    R.drawable.wallpaper5
                )
                "Style 6" -> getBitmap(
                    context,
                    R.drawable.wallpaper6
                )
                "Style 7" -> getBitmap(
                    context,
                    R.drawable.wallpaper7
                )
                "Style 8" -> getBitmap(
                    context,
                    R.drawable.wallpaper8
                )
                "Style 9" -> getBitmap(
                    context,
                    R.drawable.wallpaper9
                )
                "Style 10" -> getBitmap(
                    context,
                    R.drawable.wallpaper10
                )
                else -> getBitmap(
                    context,
                    R.drawable.wallpaper11
                )
            }
        }
    }
}
