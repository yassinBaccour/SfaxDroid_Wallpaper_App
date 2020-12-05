package com.sami.wallpapers

import android.app.WallpaperManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import java.util.*


class WallpaperPagerAdapter(private var context: Context, private var resourceId: Int) :
    PagerAdapter() {

    override fun getCount(): Int = 99

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
        container.removeView(`object` as RelativeLayout)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewLayout =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                resourceId,
                container,
                false
            )
        val drawableRes = context.resources.getIdentifier(
            "img_" + String.format("%05d", position, Locale.US), "drawable",
            context.packageName
        )
        Picasso.get().load(drawableRes).into(viewLayout.findViewById<ImageView>(R.id.detailImage))
        viewLayout.findViewById<Button>(R.id.btn_set_wallpaper)
            .setOnClickListener { setAsWallpaper(drawableRes) }
        container.addView(viewLayout)
        return viewLayout
    }

    private fun setAsWallpaper(resourceName: Int) {
        WallpaperManager
            .getInstance(context).setResource(resourceName)
    }
}