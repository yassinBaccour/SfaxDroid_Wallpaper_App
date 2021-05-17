package com.yassin.wallpaper.feature.home.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.yassin.wallpaper.databinding.ItemHorizontalScrollBinding
import com.yassin.wallpaper.feature.home.CarouselView
import com.yassin.wallpaper.feature.home.adapter.CarouselItemAdapter

class HorizontalCarouselVH(val binding: ItemHorizontalScrollBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(
        carouselView: CarouselView,
        clickListener: (BaseWallpaperView) -> Unit
    ) {

        if (!carouselView.articleList.isNullOrEmpty()) {
            binding.txtViewEmission.text = carouselView.title
            binding.recyclerViewList.apply {
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = CarouselItemAdapter(
                    carouselView.articleList,
                    carouselView.carouselTypeEnum,
                    clickListener
                )
            }
        } else {
            itemView.visibility = View.GONE
        }
    }
}
