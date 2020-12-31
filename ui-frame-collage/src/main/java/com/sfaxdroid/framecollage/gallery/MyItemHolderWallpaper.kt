package com.sfaxdroid.framecollage.gallery

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.base.extension.loadUrlWithResize
import com.sfaxdroid.base.utils.WallpaperUtils

class MyItemHolderWallpaper internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var img: ImageView = itemView.findViewById(R.id.item_img)

    fun bindView(context: Context, wall: WallpaperObject) {
        img.loadUrlWithResize(getUrlByScreen(wall, context))
    }

    private fun getUrlByScreen(wall: WallpaperObject, context: Context): String? {
        return WallpaperUtils.getUrlFromWallpaper(wall, context)
    }

}