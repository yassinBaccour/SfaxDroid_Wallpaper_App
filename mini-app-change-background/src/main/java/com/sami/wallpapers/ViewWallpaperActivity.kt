package com.sami.wallpapers

import android.app.Activity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager

class ViewWallpaperActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_picture)
        findViewById<ViewPager>(R.id.view_pager_wallpaper).adapter =
            WallpaperPagerAdapter(this, R.layout.item_wallpaper_detail)
    }

}