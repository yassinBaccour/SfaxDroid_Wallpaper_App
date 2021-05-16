package com.yassin.wallpaper.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.yassin.wallpaper.R
import com.yassin.wallpaper.databinding.ListItemCarrouselBinding
import com.yassin.wallpaper.databinding.ListItemCarrouselCatBinding
import com.yassin.wallpaper.databinding.ListItemCategoryBinding
import com.yassin.wallpaper.feature.home.CarouselTypeEnum
import com.yassin.wallpaper.feature.home.viewholder.WallpaperCarouselCatItemVH
import com.yassin.wallpaper.feature.home.viewholder.WallpaperCarouselItemVH

class CarouselItemAdapter(
    var list: List<BaseWallpaperView>,
    var type: CarouselTypeEnum = CarouselTypeEnum.LWP,
    var clickListener: (BaseWallpaperView) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_CAT ->
                return WallpaperCarouselItemVH(
                    ListItemCarrouselBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else -> return WallpaperCarouselCatItemVH(
                ListItemCarrouselCatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int) = when (type) {
        CarouselTypeEnum.LWP -> TYPE_LWP
        CarouselTypeEnum.CATEGORY -> TYPE_CAT
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WallpaperCarouselItemVH -> holder.bind(
                list[position], clickListener
            )
        }
    }

    companion object {
        const val TYPE_LWP = 1
        const val TYPE_CAT = 2
    }
}
