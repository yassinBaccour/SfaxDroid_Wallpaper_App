package com.sfaxdroid.timer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.base.extension.loadUrl
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.timer.databinding.ListItemWallpaperBinding

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
            ListItemWallpaperBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

class WallpaperImgVH(var binding: ListItemWallpaperBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        wallpaperObject: SimpleWallpaperView,
        openWallpaper: (String) -> Unit
    ) {
        binding.itemImgWallpaper.apply {
            loadUrl(wallpaperObject.thumbnailUrl)
            setOnClickListener { openWallpaper(wallpaperObject.detailUrl) }
        }
    }
}
