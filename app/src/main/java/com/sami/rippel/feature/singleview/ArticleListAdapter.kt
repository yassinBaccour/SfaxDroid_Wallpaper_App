package com.sami.rippel.feature.singleview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sami.rippel.allah.R
import com.sami.rippel.model.entity.WallpaperObject

class ArticleListAdapter(
    private var articleList: List<ItemWrapperList<Any>>,
    private var openWallpaper: (WallpaperObject) -> Unit,
    private val openCategory: (WallpaperObject, CarouselTypeEnum) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private fun getElementType(position: Int) =
        articleList[position].itemType

    override fun getItemCount() = articleList.size

    override fun getItemViewType(position: Int) = getElementType(position)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WallpaperImgVH -> holder.bind(
                articleList[position].getObject() as WallpaperObject,
                openWallpaper
            )
            is HorizontalCarouselVH -> holder.bindView(
                articleList[position].getObject() as CarouselView,
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
                        R.layout.list_item,
                        parent, false
                    )
                )


            else -> return WallpaperImgVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item,
                    parent, false
                )
            )
        }
    }

    companion object {
        const val TYPE_WALLPAPER = 1
        const val TYPE_CAROUSEL = 2
        const val TYPE_ADS = 4
    }

}

