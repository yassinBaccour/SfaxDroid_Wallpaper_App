package com.sfaxdroid.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.list.databinding.ListItemWithDetailBinding

class LwpItemVH(var binding: ListItemWithDetailBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        wallpaperObject: LwpItem,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        binding.apply {
            itemDetailTitle.text = wallpaperObject.name
            itemDetailDesc.text = wallpaperObject.desc
            itemDetailImage.apply {
                loadUrl(wallpaperObject.thumbnailUrl)
                setOnClickListener { openWallpaper(wallpaperObject) }
            }
            root.setOnClickListener { openWallpaper(wallpaperObject) }
        }

    }
}
