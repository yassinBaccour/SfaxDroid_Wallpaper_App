package com.yassin.wallpaper.feature.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem
import com.yassin.wallpaper.databinding.ListItemWithDetailBinding

class LabItemVH(var binding: ListItemWithDetailBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        wallpaperObject: CategoryItem,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        binding.apply {
            itemDetailImage.loadUrl(wallpaperObject.thumbnailUrl)
            root.setOnClickListener { openWallpaper(wallpaperObject) }
            itemDetailTitle.text = wallpaperObject.name
            itemDetailDesc.text = wallpaperObject.desc
        }
    }
}
