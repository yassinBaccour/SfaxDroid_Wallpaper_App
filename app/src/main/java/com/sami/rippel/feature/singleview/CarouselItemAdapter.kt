package com.sami.rippel.feature.singleview

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sami.rippel.allah.R
import com.sami.rippel.model.entity.WallpaperObject

class CarouselItemAdapter(
    var list: List<WallpaperObject>,
    var type: CarouselTypeEnum = CarouselTypeEnum.LIVEWALLPAPER,
    var clickListener: (WallpaperObject, CarouselTypeEnum) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_LIVE_WALLPAPER ->
                return WallpaperCarrouselItemVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.lit_item_carrousel,
                        parent, false
                    )
                )
            TYPE_CATEGORY ->
                return WallpaperCarrouselItemVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.lit_item_carrousel_cat,
                        parent, false
                    )
                )
            else -> return WallpaperCarrouselItemVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.lit_item_carrousel,
                    parent, false
                )
            )
        }
    }


    override fun getItemViewType(position: Int) = when (type) {
        CarouselTypeEnum.LIVEWALLPAPER -> TYPE_LIVE_WALLPAPER
        CarouselTypeEnum.CATEGORY -> TYPE_CATEGORY
    }


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WallpaperCarrouselItemVH -> holder.bind(
                list[position], type, clickListener
            )
        }
    }

    companion object {
        const val TYPE_LIVE_WALLPAPER = 1
        const val TYPE_CATEGORY = 2
    }

}