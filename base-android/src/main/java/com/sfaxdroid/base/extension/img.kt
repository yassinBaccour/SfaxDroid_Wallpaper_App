package com.sfaxdroid.base.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sfaxdroid.base.utils.DeviceUtils

fun ImageView.loadUrlWithResize(url: String?) {
    Glide.with(context).load(url)
        .thumbnail(0.5f)
        .override(
            DeviceUtils.getCellWidth(context),
            DeviceUtils.getCellHeight(context)
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadUrl(url: String?) {
    Glide.with(context).load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}