package com.yassin.sfax.tawakkolalaallah

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

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
                    R.drawable.tw41
                )
                "Style 2" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 3" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 4" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw41
                )
                "Style 5" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 6" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw61
                )
                "Style 7" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 8" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw61
                )
                "Style 9" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 10" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                else -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
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
                    R.drawable.tw41
                )
                "Style 2" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw41
                )
                "Style 3" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 4" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw41
                )
                "Style 5" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 6" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw61
                )
                "Style 7" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 8" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw61
                )
                "Style 9" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                "Style 10" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
                else -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.tw21
                )
            }
        }

        fun getBackground(context: Context, pref: String): Bitmap? {
            return when (pref) {
                "Style 1" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper1
                )
                "Style 2" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper2
                )
                "Style 3" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper3
                )
                "Style 4" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper4
                )
                "Style 5" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper5
                )
                "Style 6" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper6
                )
                "Style 7" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper7
                )
                "Style 8" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper8
                )
                "Style 9" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper9
                )
                "Style 10" -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper10
                )
                else -> BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.wallpaper11
                )
            }
        }
    }
}