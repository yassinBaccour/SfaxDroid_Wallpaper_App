package com.yassin.wallpaper.feature.home.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sami.rippel.allah.R
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.SimpleWallpaperView

class WallpaperImgVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.item_img_wallpaper)

    fun bind(
        wallpaperObject: SimpleWallpaperView,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        img.loadUrl(wallpaperObject.thumbnailUrl)
        img.setOnClickListener { openWallpaper(wallpaperObject) }
    }
}