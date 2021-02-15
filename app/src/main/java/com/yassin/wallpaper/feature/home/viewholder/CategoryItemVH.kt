package com.yassin.wallpaper.feature.home.viewholder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sami.rippel.allah.R
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem

class CategoryItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.item_detail_image)
    private val btnOpen: TextView = itemView.findViewById(R.id.item_detail_btn)
    private val title: TextView = itemView.findViewById(R.id.item_detail_title)
    private val desc: TextView = itemView.findViewById(R.id.item_detail_desc)

    fun bind(
        categoryItem: CategoryItem,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        img.loadUrl(categoryItem.thumbnailUrl)
        img.setOnClickListener { openWallpaper(categoryItem) }
        btnOpen.setOnClickListener { openWallpaper(categoryItem) }
        btnOpen.text = itemView.context.getString(R.string.open_cat)
        title.text = categoryItem.name
        desc.text = categoryItem.desc
        img.setBackgroundColor(Color.parseColor(categoryItem.color))
        img.scaleType = ImageView.ScaleType.CENTER
    }

}