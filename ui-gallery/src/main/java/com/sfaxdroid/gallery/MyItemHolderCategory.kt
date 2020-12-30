package com.sfaxdroid.gallery

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sfaxdroid.base.utils.DeviceUtils.Companion.getCellHeight
import com.sfaxdroid.base.utils.DeviceUtils.Companion.getCellWidth
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.base.utils.WallpaperUtils

class MyItemHolderCategory internal constructor(
    itemView: View
) :
    RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.item_img)
    private val content: LinearLayout = itemView.findViewById(R.id.contentImg)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val desc: TextView = itemView.findViewById(R.id.subtitle)

    fun bindView(context: Context, wallpaperObject: WallpaperObject) {
        Glide.with(context).load(getUrlByScreen(wallpaperObject, context))
            .thumbnail(0.5f)
            .override(
                getCellWidth(context),
                getCellHeight(context)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(img)

        content.setBackgroundColor(
            Color.parseColor(
                wallpaperObject.color
            )
        )

        title.text = wallpaperObject.name
        desc.text = wallpaperObject.desc
    }

    private fun getUrlByScreen(wall: WallpaperObject, context: Context): String? {
        return WallpaperUtils.getUrlFromWallpaper(wall, context)
    }
}