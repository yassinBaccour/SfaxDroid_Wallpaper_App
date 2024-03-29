package com.yassin.wallpaper.feature.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem
import com.sfaxdroid.data.mappers.LwpItem
import com.yassin.wallpaper.databinding.ListItemCarrouselBinding
import com.yassin.wallpaper.databinding.ListItemCarrouselCatBinding

class WallpaperCarouselCatItemVH(val binding: ListItemCarrouselCatBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        wallpaperObject: BaseWallpaperView,
        clickListener: (BaseWallpaperView) -> Unit
    ) {
        when (wallpaperObject) {
            is LwpItem -> {
                binding.txtCarrouselTitle.text = wallpaperObject.name
            }
            is CategoryItem -> {
                binding.txtCarrouselTitle.text = wallpaperObject.name
            }
        }
        binding.imgCarrouselImage.loadUrl(
            wallpaperObject.thumbnailUrl.replace(
                "category_new",
                "category_preview_new"
            )
        )
        binding.root.setOnClickListener {
            clickListener(wallpaperObject)
        }
    }
}
