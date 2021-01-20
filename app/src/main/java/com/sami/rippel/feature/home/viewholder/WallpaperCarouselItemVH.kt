package com.sami.rippel.feature.home.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sami.rippel.allah.R
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem
import com.sfaxdroid.data.mappers.LwpItem

class WallpaperCarouselItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.imgCarrouselImage)
    private val title: TextView = itemView.findViewById(R.id.txt_carrousel_title)
    private val desc: TextView = itemView.findViewById(R.id.txt_carrousel_desc)

    fun bind(
        wallpaperObject: BaseWallpaperView,
        clickListener: (BaseWallpaperView) -> Unit
    ) {
        when (wallpaperObject) {
            is LwpItem -> {
                title.text = wallpaperObject.name
                desc.text = wallpaperObject.desc
            }
            is CategoryItem -> {
                title.text = wallpaperObject.name
                desc.text = wallpaperObject.desc
            }
        }
        img.loadUrl(wallpaperObject.thumbnailUrl.replace("category_new", "category_preview_new"))
        itemView.setOnClickListener {
            clickListener(wallpaperObject)
        }
    }
}