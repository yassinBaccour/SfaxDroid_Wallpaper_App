package com.sami.rippel.feature.singleview

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sami.rippel.allah.R
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.base.utils.WallpaperUtils

class LwpItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.imageStikers)
    private val btnOpen: TextView = itemView.findViewById(R.id.button_open)

    fun bind(
        wallpaperObject: WallpaperObject,
        pos: Int,
        openWallpaper: (WallpaperObject, Int) -> Unit
    ) {
        Glide.with(itemView.context).load(getUrlByScreen(wallpaperObject, itemView.context))
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(img)
        img.setOnClickListener { openWallpaper(wallpaperObject, pos) }
        btnOpen.setOnClickListener { openWallpaper(wallpaperObject, pos) }
        btnOpen.text = itemView.context.getString(R.string.open_lwp)
    }

    private fun getUrlByScreen(wall: WallpaperObject, context: Context): String? {
        return WallpaperUtils.getUrlFromWallpaper(wall, context)
    }
}