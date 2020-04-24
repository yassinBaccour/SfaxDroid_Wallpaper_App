package com.sami.rippel.feature.singleview

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sami.rippel.allah.R
import com.sami.rippel.model.ViewModel
import com.sami.rippel.model.entity.WallpaperObject

class WallpaperImgVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mImg: ImageView = itemView.findViewById(R.id.item_img_wallpaper)

    fun bind(
        wallpaperObject: WallpaperObject,
        pos: Int,
        openWallpaper: (WallpaperObject, Int) -> Unit
    ) {
        Glide.with(itemView.context).load(getUrlByScreen(wallpaperObject))
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mImg)

        mImg.setOnClickListener { openWallpaper(wallpaperObject, pos) }
    }

    private fun getUrlByScreen(wall: WallpaperObject): String? {
        return ViewModel.Current.getUrlFromWallpaper(wall)
    }
}