package com.yassin.wallpaper.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sami.rippel.allah.R
import com.yassin.wallpaper.feature.home.CarouselTypeEnum
import com.yassin.wallpaper.feature.home.viewholder.WallpaperCarouselItemVH
import com.sfaxdroid.data.mappers.BaseWallpaperView

class CarouselItemAdapter(
    var list: List<BaseWallpaperView>,
    var type: CarouselTypeEnum = CarouselTypeEnum.LWP,
    var clickListener: (BaseWallpaperView) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_LWP ->
                return WallpaperCarouselItemVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_carrousel,
                        parent, false
                    )
                )
            TYPE_CAT ->
                return WallpaperCarouselItemVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_carrousel_cat,
                        parent, false
                    )
                )
            else -> return WallpaperCarouselItemVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_carrousel,
                    parent, false
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
