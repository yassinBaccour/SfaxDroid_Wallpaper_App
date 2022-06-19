package com.sfaxdroid.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.list.databinding.ListItemSquareWallpaperBinding

class WallpaperSquareImgVH(var binding: ListItemSquareWallpaperBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        wallpaperObject: SimpleWallpaperView,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        binding.itemImgWallpaper.apply {
            loadUrl(wallpaperObject.thumbnailUrl)
            setOnClickListener { openWallpaper(wallpaperObject) }
        }
    }
}
