package com.yassin.wallpaper.feature.home.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yassin.wallpaper.R
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.LwpItem

class LwpItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.item_detail_image)
    private val btnOpen: TextView = itemView.findViewById(R.id.item_detail_btn)
    private val title: TextView = itemView.findViewById(R.id.item_detail_title)
    private val desc: TextView = itemView.findViewById(R.id.item_detail_desc)

    fun bind(
        wallpaperObject: LwpItem,
        openWallpaper: (BaseWallpaperView) -> Unit
    ) {
        img.loadUrl(wallpaperObject.thumbnailUrl)
        img.setOnClickListener { openWallpaper(wallpaperObject) }
        btnOpen.setOnClickListener { openWallpaper(wallpaperObject) }
        btnOpen.text = itemView.context.getString(R.string.open_lwp)
        title.text = wallpaperObject.name
        desc.text = wallpaperObject.desc
    }
}