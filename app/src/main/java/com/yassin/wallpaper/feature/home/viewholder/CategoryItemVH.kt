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

class CategoryItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.item_detail_image)
    private val title: TextView = itemView.findViewById(R.id.item_detail_title)
    private val desc: TextView = itemView.findViewById(R.id.item_detail_desc)

    fun bind(
        categoryItem: CategoryItem,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        itemView.setOnClickListener { openWallpaper(categoryItem) }
        title.text = categoryItem.name
        desc.text = categoryItem.desc
        img.apply {
            loadUrl(categoryItem.thumbnailUrl)
            setOnClickListener { openWallpaper(categoryItem) }
            setBackgroundColor(Color.parseColor(categoryItem.color))
            scaleType = ImageView.ScaleType.CENTER
        }
    }
}
