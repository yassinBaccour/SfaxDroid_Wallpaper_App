package com.sami.rippel.feature.singleview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sami.rippel.allah.R
import com.sfaxdroid.base.WallpaperObject

class WallpaperCarouselItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val img: ImageView = itemView.findViewById(R.id.imgCarrouselImage)
    private val title: TextView = itemView.findViewById(R.id.txt_carrousel_title)
    private val desc: TextView = itemView.findViewById(R.id.txt_carrousel_desc)

    fun bind(
        wallpaperObject: WallpaperObject,
        type: CarouselTypeEnum,
        clickListener: (WallpaperObject, CarouselTypeEnum) -> Unit
    ) {
        title.text = wallpaperObject.name
        desc.text = wallpaperObject.desc
        if (wallpaperObject.resourceUri > 0) {
            Glide.with(itemView.context).load(wallpaperObject.resourceUri)
                .thumbnail(0.5f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)
        } else {
            Glide.with(itemView.context)
                .load(wallpaperObject.url.replace("category_new", "category_preview_new"))
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)
        }
        itemView.setOnClickListener {
            clickListener(wallpaperObject, type)
        }
    }
}