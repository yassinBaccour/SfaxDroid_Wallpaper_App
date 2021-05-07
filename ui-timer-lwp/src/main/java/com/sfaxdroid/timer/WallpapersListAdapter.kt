package com.sfaxdroid.timer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.SimpleWallpaperView

class WallpapersListAdapter(var openWallpaper: (String) -> Unit) :
    RecyclerView.Adapter<WallpaperImgVH>() {

    var wallpaperList: MutableList<SimpleWallpaperView> = arrayListOf()

    override fun getItemCount() = wallpaperList.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<SimpleWallpaperView>) {
        wallpaperList.clear()
        wallpaperList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WallpaperImgVH, position: Int) {
        holder.bind(wallpaperList[position], openWallpaper)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperImgVH {
        return WallpaperImgVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_wallpaper,
                parent, false
            )
        )
    }
}

class WallpaperImgVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.item_img_wallpaper)

    fun bind(
        wallpaperObject: SimpleWallpaperView,
        openWallpaper: (String) -> Unit
    ) {
        img.apply {
            loadUrl(wallpaperObject.thumbnailUrl)
            setOnClickListener { openWallpaper(wallpaperObject.detailUrl) }
        }
    }
}
