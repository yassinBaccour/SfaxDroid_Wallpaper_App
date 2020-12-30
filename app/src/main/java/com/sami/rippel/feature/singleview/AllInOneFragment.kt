package com.sami.rippel.feature.singleview

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sami.rippel.allah.R
import com.sami.rippel.base.BaseFragment
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.LiveWallpaper
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.data.mappers.WallpaperToViewMapper
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.fragment_all_background.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AllInOneFragment : BaseFragment(), HasAndroidInjector {

    @Inject
    lateinit var getAllWallpapersUseCase: GetAllWallpapersUseCase

    private var wallpapersListAdapter: WallpapersListAdapter? = null

    private val fileName by lazy {
        arguments?.getString(Constants.KEY_JSON_FILE_NAME, "") ?: ""
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    var mData = ArrayList<WallpaperObject>()

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
                        try {
                            val intent = Intent(
                                activity, Class.forName("com.sfaxdroid.detail.GalleryActivity")
                            )
                            intent.putExtra(Constants.KEY_LWP_NAME, "DouaLWP")
                            startActivity(intent)
                        } catch (e: ClassNotFoundException) {
                            e.printStackTrace()
                        }
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
                                    Class.forName("com.sfaxdroid.sky.SkyLiveWallpaper")
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
                            Class.forName("com.sfaxdroid.timer.WallpaperSchedulerActivity")::class.java
                        )
                        startActivity(intent)
                    }
                    is LiveWallpaper.NameOfAllah2D -> {
                        HomeActivityNavBar.isAdsShow = true
                        HomeActivityNavBar.isAdsShow = true
                        val intent = Intent(
                            activity,
                            Class.forName("com.sfaxdroid.detail.GalleryActivity")
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
                    Class.forName("com.sfaxdroid.detail.GalleryActivity")
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

    private fun openLab() {
        val intent = Intent(
            activity,
            Class.forName("com.sfaxdroid.detail.StickersLabActivity")
        )
        startActivity(intent)
    }

    private fun getItemType(screenType: ScreenType): Int {
        return when (screenType) {
            is ScreenType.Lwp -> WallpapersListAdapter.TYPE_LWP
            is ScreenType.Wall -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is ScreenType.Cat -> WallpapersListAdapter.TYPE_CAT
        }
    }

    private fun getWallpaperList(
        mList: List<WallpaperObject>,
        screenType: ScreenType,
        isMixed: Boolean = false
    ): List<ItemWrapperList<Any>> {
        val listItem: MutableList<ItemWrapperList<Any>> = arrayListOf()
        mList.forEach {
            listItem.add(
                ItemWrapperList(
                    it,
                    getItemType(screenType)
                )
            )
        }
        if (isMixed) {
            val lwpList: List<WallpaperObject> = listOf(
                WallpaperObject(
                    getString(R.string.title_word_anim_lwp),
                    getString(R.string.DescDouaLwp),
                    resources.getColor(R.color.primary),
                    9,
                    LiveWallpaper.DouaLwp
                ), WallpaperObject(
                    getString(R.string.TitleSkyboxLwp),
                    getString(R.string.DescSkyboxLwp),
                    resources.getColor(R.color.primary),
                    9,
                    LiveWallpaper.NameOfAllah
                ), WallpaperObject(
                    getString(R.string.timer_wallpaper_name),
                    getString(R.string.global_how_to_use),
                    resources.getColor(R.color.primary),
                    9,
                    LiveWallpaper.TimerLwp
                ), WallpaperObject(
                    getString(R.string.nameofallah2dtitle),
                    getString(R.string.nameofallah2ddesc),
                    resources.getColor(R.color.primary),
                    9,
                    LiveWallpaper.NameOfAllah2D
                )
            )
            val lwp = CarouselView("Live Wallpaper 4K", lwpList, CarouselTypeEnum.LIVEWALLPAPER)
            listItem.add(6, ItemWrapperList(lwp, WallpapersListAdapter.TYPE_CAROUSEL))

            //todo fix
            /*
        val wallpaperObjects =
            ViewModel.Current.getWallpaperCategoryFromName("ImageCategoryNew")
                .getGetWallpapersList()

        val cat = CarouselView("All Category", wallpaperObjects, CarouselTypeEnum.CATEGORY)
        listItem.add(13, ItemWrapperList(cat, ArticleListAdapter.TYPE_CAROUSEL))

         */
        }
        return listItem
    }

    private fun showContent(mList: List<SimpleWallpaperView>, screenType: ScreenType) {
        val list = mList.map {
            WallpaperObject(it.url)
        }
        mData.clear()
        mData = ArrayList(list)
        wallpapersListAdapter = WallpapersListAdapter(
            getWallpaperList(mData, screenType),
            { catItem, pos -> openWallpaper(catItem, pos) },
            { catItem, type -> openCategory(catItem, type) }
        )

        recycler_view_wallpapers?.apply {
            layoutManager = getLwpLayoutManager()
            setHasFixedSize(true)
            adapter = wallpapersListAdapter
            addOnItemTouchListener(
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

    private fun getLwpLayoutManager(): LayoutManager {
        return GridLayoutManager(
            context,
            3
        ).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (wallpapersListAdapter?.getItemViewType(position)) {
                        WallpapersListAdapter.TYPE_SQUARE_WALLPAPER -> 1
                        WallpapersListAdapter.TYPE_WALLPAPER -> 1
                        WallpapersListAdapter.TYPE_ADS -> spanCount
                        WallpapersListAdapter.TYPE_LWP -> spanCount
                        WallpapersListAdapter.TYPE_CAT -> spanCount
                        WallpapersListAdapter.TYPE_CAROUSEL -> spanCount
                        else -> -1
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch {
            getAllWallpapersUseCase(GetAllWallpapersUseCase.Param(fileName))
            {
                when (it) {
                    is Response.SUCESS -> {
                        val rep = (it.response as Response.SUCESS).response as WallpaperResponse
                        var screenType = getType(rep.wallpaperList.title)
                        val list = rep.wallpaperList.wallpapers.map {
                            WallpaperToViewMapper().map(it)
                        }
                        showContent(list, screenType)
                    }
                    is Response.FAILURE -> {

                    }
                }
            }
        }
    }

    private fun getType(type: String): ScreenType {
        return when (type) {
            "LWP" -> ScreenType.Lwp()
            else -> ScreenType.Wall()
        }
    }

    sealed class ScreenType {
        class Lwp : ScreenType()
        class Wall : ScreenType()
        class Cat : ScreenType()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_background, container, false)
    }

    companion object {
        fun newInstance(fileName: String): AllInOneFragment {
            return AllInOneFragment().apply {
                val args = Bundle()
                args.putString(Constants.KEY_JSON_FILE_NAME, fileName)
                arguments = args
            }
        }
    }

}