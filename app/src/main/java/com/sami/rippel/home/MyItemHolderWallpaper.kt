package com.sami.rippel.home

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sami.rippel.allah.R
import com.sami.rippel.model.ViewModel
import com.sfaxdroid.base.DeviceUtils.Companion.getCellHeight
import com.sfaxdroid.base.DeviceUtils.Companion.getCellWidth
import com.sfaxdroid.base.WallpaperObject

class MyItemHolderWallpaper internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var mImg: ImageView = itemView.findViewById(R.id.item_img)

    fun bindView(context: Context, wall: WallpaperObject) {
        Glide.with(context).load(getUrlByScreen(wall, context))
            .thumbnail(0.5f)
            .override(
                getCellWidth(context),
                getCellHeight(context)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mImg)
    }

    private fun getUrlByScreen(wall: WallpaperObject, context: Context): String? {
        return ViewModel.Current.getUrlFromWallpaper(wall, context)
    }

}