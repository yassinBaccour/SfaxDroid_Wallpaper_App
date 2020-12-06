package com.sami.rippel.feature.singleview

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sami.rippel.allah.R
import com.sami.rippel.base.BaseFragment
import com.sami.rippel.livewallpapers.lwpskyview.SkyNameOfAllahLiveWallpaper
import com.sami.rippel.livewallpapers.lwptimer.WallpaperSchedulerActivity
import com.sami.rippel.model.Constants
import com.sami.rippel.model.ViewModel
import com.sami.rippel.model.entity.StateEnum
import com.sami.rippel.model.entity.WallpaperObject
import com.sami.rippel.model.listner.OnStateChangeListener
import com.sami.rippel.presenter.AllWallpaperPresenter
import com.sami.rippel.presenter.Contract.WallpaperFragmentContract
import com.sami.rippel.ui.activity.DetailsActivity
import com.sami.rippel.ui.activity.GalleryActivity
import java.util.*

class AllInOneFragment : BaseFragment<AllWallpaperPresenter?>(),
    WallpaperFragmentContract.View, OnStateChangeListener {

    private var adapter: ArticleListAdapter? = null

    override fun getFragmentActivity(): Activity {
        return activity!!
    }

    override fun fillForm() {}

    override fun instantiatePresenter(): AllWallpaperPresenter {
        return AllWallpaperPresenter(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ViewModel.Current.unregisterOnStateChangeListener(this)
    }

    override fun initEventAndData() {
        mPresenter?.getWallpaper()
    }

    private fun openWallpaper(wallpaperObject: WallpaperObject, pos: Int) {
        showDetailViewActivity(wallpaperObject)
    }

    private fun openCategory(wallpaperObject: WallpaperObject, carouselTypeEnum: CarouselTypeEnum) {
        when (carouselTypeEnum) {
            is CarouselTypeEnum.LIVEWALLPAPER -> {
                when (wallpaperObject.liveWallpaper) {
                    is LiveWallpaper.DouaLwp -> {
                        HomeActivityNavBar.isAdsShow = true
                        val intent = Intent(
                            activity,
                            GalleryActivity::class.java
                        )
                        intent.putExtra("LwpName", "DouaLWP")
                        startActivity(intent)
                    }
                    is LiveWallpaper.NameOfAllah -> {
                        HomeActivityNavBar.isAdsShow = true
                        try {
                            if (mListener != null) {
                                mListener.onTrackAction("LwpFragment", "OpenSkyBox")
                            }
                            val intent = Intent(
                                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                            )
                            intent.putExtra(
                                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                ComponentName(
                                    activity!!,
                                    SkyNameOfAllahLiveWallpaper::class.java
                                )
                            )
                            startActivity(intent)
                        } catch (e: Exception) {
                            try {
                                val intent = Intent()
                                intent.action = WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER
                                startActivity(intent)
                            } catch (ignored: Exception) {
                            }
                        }
                    }
                    is LiveWallpaper.TimerLwp -> {
                        HomeActivityNavBar.isAdsShow = true
                        val intent = Intent(
                            activity,
                            WallpaperSchedulerActivity::class.java
                        )
                        startActivity(intent)
                    }
                    is LiveWallpaper.NameOfAllah2D -> {
                        HomeActivityNavBar.isAdsShow = true
                        HomeActivityNavBar.isAdsShow = true
                        val intent = Intent(
                            activity,
                            GalleryActivity::class.java
                        )
                        intent.putExtra("LwpName", "NameOfAllah2DLWP")
                        startActivity(intent)
                    }
                }
            }
            is CarouselTypeEnum.CATEGORY -> {
                val categoryName: String = wallpaperObject.name
                if (mListener != null) {
                    HomeActivityNavBar.nbOpenAds++
                    mListener.onOpenScreenTracker("CategoryGallery")
                    mListener.onTrackAction("CategoryOpen", categoryName)
                }
                val intent = Intent(
                    activity,
                    GalleryActivity::class.java
                )
                intent.putParcelableArrayListExtra(
                    Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, mData
                )

                if (categoryName.isNotEmpty()) intent.putExtra(
                    Constants.DETAIL_IMAGE_POS,
                    categoryName
                )
                startActivity(intent)
            }
        }
    }

    private fun getWallpaperList(mList: List<WallpaperObject>): List<ItemWrapperList<Any>> {
        val listItem: MutableList<ItemWrapperList<Any>> = arrayListOf()
        mList.forEach { listItem.add(ItemWrapperList(it, ArticleListAdapter.TYPE_WALLPAPER)) }
        val lwpList: List<WallpaperObject> = listOf(
            WallpaperObject(
                getString(R.string.TitleDouaLwp),
                getString(R.string.DescDouaLwp),
                resources.getColor(R.color.primary),
                R.mipmap.ic_doua_lwp,
                LiveWallpaper.DouaLwp
            ), WallpaperObject(
                getString(R.string.TitleSkyboxLwp),
                getString(R.string.DescSkyboxLwp),
                resources.getColor(R.color.primary),
                R.mipmap.ic_skybox,
                LiveWallpaper.NameOfAllah
            ), WallpaperObject(
                getString(R.string.TitleTimerLwp),
                getString(R.string.DescTimerLwp),
                resources.getColor(R.color.primary),
                R.mipmap.ic_timer_lwp,
                LiveWallpaper.TimerLwp
            ), WallpaperObject(
                getString(R.string.nameofallah2dtitle),
                getString(R.string.nameofallah2ddesc),
                resources.getColor(R.color.primary),
                R.mipmap.ic_nameofallah_lwp,
                LiveWallpaper.NameOfAllah2D
            )
        )
        val lwp = CarouselView("Live Wallpaper 4K", lwpList, CarouselTypeEnum.LIVEWALLPAPER)
        listItem.add(6, ItemWrapperList(lwp, ArticleListAdapter.TYPE_CAROUSEL))

        val wallpaperObjects =
            ViewModel.Current.getWallpaperCategoryFromName("ImageCategoryNew")
                .getGetWallpapersList()

        val cat = CarouselView("All Category", wallpaperObjects, CarouselTypeEnum.CATEGORY)
        listItem.add(13, ItemWrapperList(cat, ArticleListAdapter.TYPE_CAROUSEL))

        return listItem
    }

    override fun showContent(mList: List<WallpaperObject>) {
        if (ViewModel.Current.isWallpapersLoaded) {
            mData.clear()
            mData = ArrayList(mList)
            if (activity != null && ViewModel.Current.device.isConnected(
                    activity
                ) && mData != null && mData.size > 0
            ) {
                adapter = ArticleListAdapter(
                    getWallpaperList(mData),
                    { catItem, pos -> openWallpaper(catItem, pos) },
                    { catItem, type -> openCategory(catItem, type) }
                )
                mRecyclerView.adapter = adapter
                mRecyclerView.addOnItemTouchListener(
                    object : RecyclerView.OnItemTouchListener {
                        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                        override fun onInterceptTouchEvent(
                            rv: RecyclerView, e:
                            MotionEvent
                        ): Boolean {
                            if (e.action == MotionEvent.ACTION_DOWN &&
                                rv.scrollState == RecyclerView.SCROLL_STATE_SETTLING
                            ) {
                                rv.stopScroll()
                            }
                            return false
                        }

                        override fun onRequestDisallowInterceptTouchEvent(
                            disallowIntercept: Boolean
                        ) {
                        }
                    })
            } else {
                shoNowInternet()
            }
        }
    }

    private fun showDetailViewActivity(wallpaperObject: WallpaperObject) {
        if (view != null) {

            val pos = mData.indexOfFirst { it.url == wallpaperObject.url }
            HomeActivityNavBar.nbOpenAds++
            mListener?.onTrackAction("AllFragment", "OpenWallpapers")
            Intent(
                activity,
                DetailsActivity::class.java
            ).apply {
                putParcelableArrayListExtra(
                    Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER,
                    mData
                )
                putExtra(
                    Constants.DETAIL_IMAGE_POS,
                    pos
                )
                startActivity(this)
            }
        }
    }

    private fun shoNowInternet() {
        view?.let {
            Toast.makeText(
                activity, getString(R.string.NoConnection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun getLayoutManager(): LayoutManager {
        return GridLayoutManager(
            context,
            3
        ).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter?.getItemViewType(position)) {
                        ArticleListAdapter.TYPE_WALLPAPER -> 1
                        ArticleListAdapter.TYPE_ADS -> 1
                        ArticleListAdapter.TYPE_CAROUSEL -> spanCount
                        else -> -1
                    }
                }
            }
        }
    }

    override fun onStateChange(state: StateEnum) {
        mPresenter?.getWallpaper()
    }

    override fun showSnackMsg(msg: String) {}
    override fun showLoading() {}
    override fun hideLoading() {}
    override val fragmentId = R.layout.fragment_all_background

    companion object {
        fun newInstance(): AllInOneFragment {
            return AllInOneFragment()
        }
    }

}