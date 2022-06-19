package com.sfaxdroid.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.CategoryItem
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.list.CarouselView
import com.sfaxdroid.data.mappers.ItemWrapperList
import com.sfaxdroid.list.databinding.ItemHorizontalScrollBinding
import com.sfaxdroid.list.databinding.ListItemCategoryBinding
import com.sfaxdroid.list.databinding.ListItemSquareWallpaperBinding
import com.sfaxdroid.list.databinding.ListItemWallpaperBinding
import com.sfaxdroid.list.databinding.ListItemWithDetailBinding
import com.sfaxdroid.list.viewholder.*

class WallpapersListAdapter(
    private var articleList: MutableList<ItemWrapperList>,
    private val openCategory: (BaseWallpaperView) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private fun getElementType(position: Int) =
        articleList[position].itemType

    override fun getItemCount() = articleList.size

    override fun getItemViewType(position: Int) = getElementType(position)

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<ItemWrapperList>) {
        if (list.isNotEmpty()) {
            articleList.clear()
            articleList = list.toMutableList()
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WallpaperImgVH -> holder.bind(
                articleList[position].`object` as SimpleWallpaperView,
                openCategory
            )
            is WallpaperSquareImgVH -> holder.bind(
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
                    ListItemWallpaperBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            TYPE_CAROUSEL -> return HorizontalCarouselVH(
                ItemHorizontalScrollBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            TYPE_ADS ->
                return WallpaperImgVH(
                    ListItemWallpaperBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            TYPE_SQUARE_WALLPAPER ->
                return WallpaperSquareImgVH(
                    ListItemSquareWallpaperBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            TYPE_LWP ->
                return LwpItemVH(
                    ListItemWithDetailBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            TYPE_CAT ->
                return CategoryItemVH(
                    ListItemCategoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            TYPE_LAB ->
                return LabItemVH(
                    ListItemWithDetailBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            else -> return WallpaperImgVH(
                ListItemWallpaperBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
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
