package com.sami.rippel.feature.singleview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.sfaxdroid.base.WallpaperObject
import kotlinx.android.synthetic.main.item_horizontal_scroll.view.*

class HorizontalCarouselVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var recyclerView = itemView.recycler_view_list
    private var title = itemView.txt_view_emission

    fun bindView(
        carouselView: CarouselView,
        clickListener: (WallpaperObject, CarouselTypeEnum) -> Unit
    ) {
        if (!carouselView.articleList.isNullOrEmpty()) {
            title.text = carouselView.title
            recyclerView.apply {
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