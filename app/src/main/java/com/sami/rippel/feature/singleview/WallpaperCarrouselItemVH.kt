package com.sami.rippel.feature.singleview

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sami.rippel.allah.R
import com.sami.rippel.model.ViewModel
import com.sami.rippel.model.entity.WallpaperObject

class WallpaperCarrouselItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mImg: ImageView = itemView.findViewById(R.id.imgCarrouselImage)
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
                .into(mImg)
        } else {
            Glide.with(itemView.context)
                .load(wallpaperObject.url.replace("category_new", "category_preview_new"))
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImg)
        }

        itemView.setOnClickListener {
            clickListener(wallpaperObject, type)
        }
    }
}