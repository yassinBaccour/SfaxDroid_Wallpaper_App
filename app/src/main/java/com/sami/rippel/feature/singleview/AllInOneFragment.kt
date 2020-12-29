package com.sami.rippel.feature.singleview

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sami.rippel.allah.R
import com.sami.rippel.base.BaseFragment
import com.sami.rippel.model.ViewModel
import com.sfaxdroid.bases.StateEnum
import com.sfaxdroid.bases.OnStateChangeListener
import com.sami.rippel.feature.main.presenter.AllWallpaperPresenter
import com.sami.rippel.feature.main.presenter.Contract.WallpaperFragmentContract
import com.sfaxdroid.gallery.GalleryActivity
import com.sfaxdroid.base.DeviceUtils
import com.sfaxdroid.base.LiveWallpaper
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject

class AllInOneFragment : BaseFragment<AllWallpaperPresenter?>(),
    WallpaperFragmentContract.View, OnStateChangeListener, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var repo: GetAllWallpapersUseCase

    private var adapter: ArticleListAdapter? = null

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun getFragmentActivity(): Activity {
        return requireActivity()
    }

    override fun fillForm() {}

    override fun instantiatePresenter(): AllWallpaperPresenter {
        return AllWallpaperPresenter()
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

    private fun openCategory(
        wallpaperObject: WallpaperObject,
        carouselTypeEnum: CarouselTypeEnum
    ) {
        when (carouselTypeEnum) {
            is CarouselTypeEnum.LIVEWALLPAPER -> {
                when (wallpaperObject.liveWallpaper) {
                    is LiveWallpaper.DouaLwp -> {
                        HomeActivityNavBar.isAdsShow = true
                        val intent = Intent(
                            activity,
                            GalleryActivity::class.java
                        )
                        intent.putExtra(com.sfaxdroid.base.Constants.KEY_LWP_NAME, "DouaLWP")
                        startActivity(intent)
                    }
                    is LiveWallpaper.NameOfAllah -> {
                        HomeActivityNavBar.isAdsShow = true
                        try {
                            val intent = Intent(
                                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                            )
                            intent.putExtra(
                                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                ComponentName(
                                    requireActivity(),
                                    com.sfaxdroid.sky.SkyLiveWallpaper::class.java
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
                            com.sfaxdroid.timer.WallpaperSchedulerActivity::class.java
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
                        intent.putExtra(
                            com.sfaxdroid.base.Constants.KEY_LWP_NAME,
                            "NameOfAllah2DLWP"
                        )
                        startActivity(intent)
                    }
                }
            }
            is CarouselTypeEnum.CATEGORY -> {
                val categoryName: String = wallpaperObject.name
                HomeActivityNavBar.nbOpenAds++
                val intent = Intent(
                    activity,
                    GalleryActivity::class.java
                )
                intent.putParcelableArrayListExtra(
                    com.sfaxdroid.base.Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, mData
                )

                if (categoryName.isNotEmpty()) intent.putExtra(
                    com.sfaxdroid.base.Constants.DETAIL_IMAGE_POS,
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
                getString(R.string.title_word_anim_lwp),
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
                getString(R.string.timer_wallpaper_name),
                getString(R.string.global_how_to_use),
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
            if (activity != null && DeviceUtils.isConnected(
                    requireContext()
                ) == true && mData != null && mData.size > 0
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
            Intent(
                activity,
                Class.forName("com.sfaxdroid.detail.DetailsActivity")
            ).apply {
                putParcelableArrayListExtra(
                    com.sfaxdroid.base.Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER,
                    mData
                )
                putExtra(
                    com.sfaxdroid.base.Constants.DETAIL_IMAGE_POS,
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