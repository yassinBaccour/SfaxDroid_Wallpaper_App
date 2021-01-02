package com.sami.rippel.feature.home.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sami.rippel.allah.R
import com.sfaxdroid.base.extension.loadUrl
import com.sami.rippel.utils.WallpaperUtils
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.SimpleWallpaperView

class WallpaperImgVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.item_img_wallpaper)

    fun bind(
        wallpaperObject: SimpleWallpaperView,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        img.loadUrl(getUrlByScreen(wallpaperObject, itemView.context))
        img.setOnClickListener { openWallpaper(wallpaperObject) }
    }

    private fun getUrlByScreen(wall: BaseWallpaperView, context: Context): String? {
        return WallpaperUtils.getUrlFromWallpaper(wall.url, context)
    }
}