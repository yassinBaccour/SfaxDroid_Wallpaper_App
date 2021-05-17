package com.yassin.wallpaper.feature.home.viewholder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem
import com.yassin.wallpaper.R
import com.yassin.wallpaper.databinding.ListItemCategoryBinding

class CategoryItemVH(var binding: ListItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        categoryItem: CategoryItem,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        binding.apply {
            itemDetailTitle.text = categoryItem.name
            itemDetailDesc.text = categoryItem.desc
            itemDetailImage.apply {
                loadUrl(categoryItem.thumbnailUrl)
                setOnClickListener { openWallpaper(categoryItem) }
                setBackgroundColor(Color.parseColor(categoryItem.color))
                scaleType = ImageView.ScaleType.CENTER
            }
            root.setOnClickListener { openWallpaper(categoryItem) }
        }
    }
}
