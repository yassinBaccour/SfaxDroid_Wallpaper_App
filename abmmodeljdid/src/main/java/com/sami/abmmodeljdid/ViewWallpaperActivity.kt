package com.sami.abmmodeljdid

import android.app.Activity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager

class ViewWallpaperActivity : Activity() {

    private val adapter by lazy { WallpaperPagerAdapter(this, R.layout.layout_detail_pager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_picture)
        findViewById<ViewPager>(R.id.view_pager_wallpaper).setAdapter(adapter)
    }

}