package com.sami.rippel.feature.singleview

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sami.rippel.allah.R
import com.sami.rippel.model.ViewModel
import com.sfaxdroid.base.WallpaperObject

class WallpaperImgVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mImg: ImageView = itemView.findViewById(R.id.item_img_wallpaper)

    fun bind(
        wallpaperObject: WallpaperObject,
        pos: Int,
        openWallpaper: (WallpaperObject, Int) -> Unit
    ) {
        Glide.with(itemView.context).load(getUrlByScreen(wallpaperObject, itemView.context))
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mImg)

        mImg.setOnClickListener { openWallpaper(wallpaperObject, pos) }
    }

    private fun getUrlByScreen(wall: WallpaperObject, context: Context): String? {
        return ViewModel.Current.getUrlFromWallpaper(wall, context)
    }
}