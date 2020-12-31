package com.sami.rippel.feature.home

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
import com.sami.rippel.core.base.BaseFragment
import com.sami.rippel.feature.home.adapter.WallpapersListAdapter
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.utils.FileUtils
import com.sfaxdroid.data.entity.LiveWallpaper
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.*
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import com.sfaxdroid.domain.GetCategoryUseCase
import com.sfaxdroid.domain.GetLiveWallpapersUseCase
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

    @Inject
    lateinit var getLiveWallpapersUseCase: GetLiveWallpapersUseCase

    @Inject
    lateinit var getCategoryUseCase: GetCategoryUseCase

    private var wallpapersListAdapter: WallpapersListAdapter? = null

    private val fileName by lazy {
        arguments?.getString(Constants.KEY_JSON_FILE_NAME, "") ?: ""
    }

    private val screen by lazy {
        arguments?.getString(Constants.KEY_SCREEN_TYPE, "") ?: ""
    }

    private val selectedLwpName by lazy {
        arguments?.getString(Constants.KEY_LWP_NAME, "") ?: ""
    }


    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    var mData = ArrayList<BaseWallpaperView>()

    private fun openByClassName(name: String, url: String) {
        try {
            startActivity(Intent(
                context,
                Class.forName(name)
            ).apply {
                putExtra(
                    Constants.URL_TO_DOWNLOAD,
                    url
                )
            })
        } catch (e: Exception) {
        }
    }

    private fun openByType(wallpaperObject: SimpleWallpaperView) {
        when (selectedLwpName) {
            Constants.KEY_WORD_IMG_LWP -> {
                openByClassName("com.sfaxdoird.anim.img.WordImgActivity", wallpaperObject.url)
            }
            Constants.KEY_ANIM_2D_LWP -> {
                openByClassName("com.sfaxdoird.anim.word.AnimWord2dActivity", wallpaperObject.url)
            }
            Constants.KEY_RIPPLE_LWP -> {
                showDetailViewActivity(wallpaperObject, Constants.KEY_RIPPLE_LWP)
            }
            Constants.KEY_ADD_TIMER_LWP -> {
                showDetailViewActivity(wallpaperObject, Constants.KEY_ADD_TIMER_LWP)
            }
            Constants.KEY_ADDED_LIST_TIMER_LWP -> {
                showDetailViewActivity(wallpaperObject, Constants.KEY_ADDED_LIST_TIMER_LWP)
            }
            else -> {
                showDetailViewActivity(wallpaperObject)
            }
        }
    }


    private fun openWallpaper(wallpaperObject: BaseWallpaperView) {
        when (wallpaperObject) {
            is SimpleWallpaperView -> {
                openByType(wallpaperObject)
            }
            is LwpItem -> {
                openLwp(wallpaperObject)
            }
            is CategoryItem -> {
                val categoryName: String = wallpaperObject.name
                HomeActivityNavBar.nbOpenAds++
                val intent = Intent(
                    activity,
                    Class.forName("com.sfaxdroid.framecollage.gallery.GalleryActivity")
                )
                intent.putParcelableArrayListExtra(
                    Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, arrayListOf()
                )
                if (categoryName.isNotEmpty()) intent.putExtra(
                    Constants.DETAIL_IMAGE_POS,
                    categoryName
                )
                startActivity(intent)
            }
        }
    }

    private fun openLwp(
        wallpaperObject: LwpItem,
    ) {
        when (wallpaperObject.type) {
            is LiveWallpaper.DouaLwp -> {
                HomeActivityNavBar.isAdsShow = true
                try {
                    val intent = Intent(
                        activity,
                        Class.forName("com.sfaxdroid.framecollage.gallery.GalleryActivity")
                    )
                    intent.putExtra(Constants.KEY_LWP_NAME, "DouaLWP")
                    startActivity(intent)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }
            is LiveWallpaper.SkyView -> {
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
                    Class.forName("com.sfaxdroid.framecollage.gallery.GalleryActivity")
                )
                intent.putExtra(
                    Constants.KEY_LWP_NAME,
                    "NameOfAllah2DLWP"
                )
                startActivity(intent)
            }
            LiveWallpaper.none -> {

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
            is ScreenType.Lab -> WallpapersListAdapter.TYPE_LAB
            is ScreenType.TEXTURE -> WallpapersListAdapter.TYPE_LWP
            is ScreenType.TIMER -> WallpapersListAdapter.TYPE_LWP
        }
    }

    private fun getWallpaperList(
        mList: List<BaseWallpaperView>,
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
            val lwpList: List<LwpItem> = arrayListOf()
            val lwp = CarouselView("Live Wallpaper 4K", lwpList, CarouselTypeEnum.LWP)
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

    private fun showContent(mList: List<BaseWallpaperView>, screenType: ScreenType) {
        mData.clear()
        mData = ArrayList(mList)
        wallpapersListAdapter = WallpapersListAdapter(
            getWallpaperList(mList, screenType)
        ) { catItem -> openWallpaper(catItem) }

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

    private fun showDetailViewActivity(wallpaperObject: SimpleWallpaperView, lwpName: String = "") {
        if (view != null) {
            val pos = mData.indexOfFirst { it.url == wallpaperObject.url }
            HomeActivityNavBar.nbOpenAds++
            Intent(
                activity,
                Class.forName("com.sfaxdroid.detail.DetailsActivity")
            ).apply {
                putParcelableArrayListExtra(
                    Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER,
                    arrayListOf()
                )
                putExtra(
                    Constants.DETAIL_IMAGE_POS,
                    pos
                )
                if (lwpName.isNotEmpty())
                    putExtra(
                        Constants.KEY_LWP_NAME,
                        lwpName
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

    private fun getLiveWallpapers(screenType: ScreenType) {
        GlobalScope.launch {
            getLiveWallpapersUseCase(GetLiveWallpapersUseCase.Param(fileName))
            {
                when (it) {
                    is Response.SUCESS -> {
                        val rep =
                            (it.response as Response.SUCESS).response as WallpaperResponse
                        val list = rep.wallpaperList.wallpapers.map {
                            WallpaperToLwpMapper().map(it)
                        }
                        showContent(list, screenType)
                    }
                    is Response.FAILURE -> {

                    }
                }
            }
        }
    }

    private fun getWallpapers(screenType: ScreenType) {
        GlobalScope.launch {
            getAllWallpapersUseCase(GetAllWallpapersUseCase.Param(fileName))
            {
                when (it) {
                    is Response.SUCESS -> {
                        val rep =
                            (it.response as Response.SUCESS).response as WallpaperResponse
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

    private fun getSavedWallpaperList(screenType: ScreenType) {
        val list = FileUtils.getPermanentDirListFiles(
            requireContext(),
            requireContext().getString(R.string.app_namenospace)
        )?.map {
            SimpleWallpaperView(it?.path.orEmpty())
        }.orEmpty()
        showContent(list, screenType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val screenType = getType(screen)
        when (screenType) {
            is ScreenType.Lwp -> {
                getLiveWallpapers(screenType)
            }
            is ScreenType.Wall -> {
                getWallpapers(screenType)
            }
            is ScreenType.Cat -> {
                GlobalScope.launch {
                    getCategoryUseCase(GetCategoryUseCase.Param(fileName))
                    {
                        when (it) {
                            is Response.SUCESS -> {
                                val rep =
                                    (it.response as Response.SUCESS).response as WallpaperResponse
                                val list = rep.wallpaperList.wallpapers.map {
                                    WallpaperToCategoryMapper().map(it)
                                }
                                showContent(list, screenType)
                            }
                            is Response.FAILURE -> {

                            }
                        }
                    }
                }
            }
            ScreenType.Lab -> {
            }
            ScreenType.TEXTURE -> {
                GlobalScope.launch {
                    getWallpapers(screenType)
                }
            }
            ScreenType.TIMER -> {
                getSavedWallpaperList(screenType)
            }
        }

    }

    private fun getType(type: String): ScreenType {
        return when (type) {
            "LWP" -> ScreenType.Lwp
            "CAT" -> ScreenType.Cat
            "LAB" -> ScreenType.Lab
            "TEXTURE" -> ScreenType.Lab
            "TIMER" -> ScreenType.TIMER
            else -> ScreenType.Wall
        }
    }

    sealed class ScreenType {
        object Lwp : ScreenType()
        object Wall : ScreenType()
        object Cat : ScreenType()
        object Lab : ScreenType()
        object TEXTURE : ScreenType()
        object TIMER : ScreenType()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_background, container, false)
    }

    companion object {
        fun newInstance(fileName: String, screenType: String): AllInOneFragment {
            return AllInOneFragment().apply {
                val args = Bundle()
                args.putString(Constants.KEY_JSON_FILE_NAME, fileName)
                args.putString(Constants.KEY_SCREEN_TYPE, screenType)
                arguments = args
            }
        }
    }

}