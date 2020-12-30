package com.sfaxdroid.gallery

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sfaxdroid.base.utils.DeviceUtils.Companion.getCellHeight
import com.sfaxdroid.base.utils.DeviceUtils.Companion.getCellWidth
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.base.utils.WallpaperUtils

class MyItemHolderWallpaper internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var img: ImageView = itemView.findViewById(R.id.item_img)

    fun bindView(context: Context, wall: WallpaperObject) {
        Glide.with(context).load(getUrlByScreen(wall, context))
            .thumbnail(0.5f)
            .override(
                getCellWidth(context),
                getCellHeight(context)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(img)
    }

    private fun getUrlByScreen(wall: WallpaperObject, context: Context): String? {
        return WallpaperUtils.getUrlFromWallpaper(wall, context)
    }

}