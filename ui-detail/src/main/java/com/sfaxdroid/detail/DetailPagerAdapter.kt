package com.sfaxdroid.detail

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sfaxdroid.bases.BaseView
import com.sfaxdroid.base.Utils.Companion.getUrlByScreenSize
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.detail.utils.TouchImageView
import java.util.*

class DetailPagerAdapter(
    private val context: Context,
    private val resourceId: Int,
    wallpaperList: ArrayList<WallpaperObject?>?,
    private val baseView: BaseView
) : BasePagerAdapter<WallpaperObject?>(wallpaperList) {

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    override fun instantiateItem(
        container: ViewGroup,
        position: Int
    ): Any {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = inflater.inflate(resourceId, container, false)

        val detailImage: TouchImageView = viewLayout
            .findViewById(R.id.detailImage)
        baseView.showLoading()
        Glide.with(context).load(getUrlByScreen(getItem(position)?.url ?: "", context))
            .into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    detailImage.setImageDrawable(resource)
                    baseView.hideLoading()
                }
            })
        container.addView(viewLayout)
        container.addView(
            TouchImageView(context), LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        return viewLayout
    }

    private fun getUrlByScreen(
        urlToChange: String,
        context: Context
    ) = getUrlByScreenSize(urlToChange, context)

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as RelativeLayout)
    }

}