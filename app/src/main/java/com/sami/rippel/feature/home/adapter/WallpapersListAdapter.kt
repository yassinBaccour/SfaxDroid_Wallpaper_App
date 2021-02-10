package com.sami.rippel.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sami.rippel.allah.R
import com.sami.rippel.feature.home.CarouselView
import com.sami.rippel.feature.home.ItemWrapperList
import com.sami.rippel.feature.home.viewholder.CategoryItemVH
import com.sami.rippel.feature.home.viewholder.HorizontalCarouselVH
import com.sami.rippel.feature.home.viewholder.LabItemVH
import com.sami.rippel.feature.home.viewholder.LwpItemVH
import com.sami.rippel.feature.home.viewholder.WallpaperImgVH
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.data.mappers.SimpleWallpaperView

class WallpapersListAdapter(
    private var articleList: List<ItemWrapperList<Any>>,
    private val openCategory: (BaseWallpaperView) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private fun getElementType(position: Int) =
        articleList[position].itemType

    override fun getItemCount() = articleList.size

    override fun getItemViewType(position: Int) = getElementType(position)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WallpaperImgVH -> holder.bind(
                articleList[position].`object` as SimpleWallpaperView,
                openCategory
            )
            is HorizontalCarouselVH -> holder.bindView(
                articleList[position].`object` as CarouselView,
                openCategory
            )
            is LwpItemVH -> holder.bind(
                articleList[position].`object` as LwpItem,
                openCategory
            )
            is CategoryItemVH -> holder.bind(
                articleList[position].`object` as CategoryItem,
                openCategory
            )
            is LabItemVH -> holder.bind(
                articleList[position].`object` as CategoryItem,
                openCategory
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {

            TYPE_WALLPAPER ->
                return WallpaperImgVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_wallpaper,
                        parent, false
                    )
                )

            TYPE_CAROUSEL -> return HorizontalCarouselVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_horizontal_scroll,
                    parent, false
                )
            )

            TYPE_ADS ->
                return WallpaperImgVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_wallpaper,
                        parent, false
                    )
                )

            TYPE_SQUARE_WALLPAPER ->
                return WallpaperImgVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_square_wallpaper,
                        parent, false
                    )
                )

            TYPE_LWP ->
                return LwpItemVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_with_detail,
                        parent, false
                    )
                )

            TYPE_CAT ->
                return CategoryItemVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_with_detail,
                        parent, false
                    )
                )

            TYPE_LAB ->
                return LabItemVH(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_with_detail,
                        parent, false
                    )
                )

            else -> return WallpaperImgVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_wallpaper,
                    parent, false
                )
            )
        }
    }

    companion object {
        const val TYPE_WALLPAPER = 1
        const val TYPE_CAROUSEL = 2
        const val TYPE_SQUARE_WALLPAPER = 3
        const val TYPE_ADS = 4
        const val TYPE_LWP = 5
        const val TYPE_CAT = 6
        const val TYPE_LAB = 7
    }

}

