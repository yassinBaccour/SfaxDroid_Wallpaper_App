package com.sami.rippel.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sami.rippel.allah.R
import com.sami.rippel.core.base.BaseFragment
import com.sami.rippel.feature.home.adapter.WallpapersListAdapter
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.utils.FileUtils
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.data.entity.LiveWallpaper
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.mappers.*
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import com.sfaxdroid.domain.GetCatWallpapersUseCase
import com.sfaxdroid.domain.GetCategoryUseCase
import com.sfaxdroid.domain.GetLiveWallpapersUseCase
import com.sfaxdroid.sky.SkyLiveWallpaper
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.fragment_all_background.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

open class AllInOneFragment : BaseFragment(), HasAndroidInjector {

    @Inject
    lateinit var getAllWallpapersUseCase: GetAllWallpapersUseCase

    @Inject
    lateinit var getLiveWallpapersUseCase: GetLiveWallpapersUseCase

    @Inject
    lateinit var getCategoryUseCase: GetCategoryUseCase

    @Inject
    lateinit var getCatWallpapersUseCase: GetCatWallpapersUseCase

    private var wallpapersListAdapter: WallpapersListAdapter? = null

    private val fileName by lazy {
        arguments?.getString("keyJsonFileName").orEmpty()
    }

    private val screen by lazy {
        arguments?.getString("keyScreenType").orEmpty()
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
                    Constants.EXTRA_URL_TO_DOWNLOAD,
                    url
                )
            })
        } catch (e: Exception) {
        }
    }

    private fun getFullUrl(url: String): String {
        return url.replace("_preview", "")
    }

    private fun openByType(wallpaperObject: SimpleWallpaperView) {
        when (selectedLwpName) {
            Constants.KEY_WORD_IMG_LWP -> {
                openByClassName(
                    "com.sfaxdoird.anim.img.WordImgActivity",
                    getFullUrl(wallpaperObject.url)
                )
            }
            Constants.KEY_ANIM_2D_LWP -> {
                openByClassName(
                    "com.sfaxdoird.anim.word.AnimWord2dActivity",
                    getFullUrl(wallpaperObject.url)
                )
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
        HomeActivityNavBar.isAdsShow = true
        when (wallpaperObject) {
            is SimpleWallpaperView -> {
                openByType(wallpaperObject)
            }
            is LwpItem -> {
                openLwp(wallpaperObject)
            }
            is CategoryItem -> {
                val cat = wallpaperObject as CategoryItem
                HomeActivityNavBar.nbOpenAds++
                findNavController().navigate(R.id.category_show_navigation_fg,
                    Bundle().apply {
                        putString(Constants.EXTRA_JSON_FILE_NAME, cat.file)
                        putString(Constants.EXTRA_SCREEN_TYPE, "CAT_WALL")
                    })
            }
        }
    }

    private fun navToTexture(lwpName: String) {
        findNavController().navigate(R.id.chooser_navigation_fg, Bundle().apply {
            putString(
                Constants.EXTRA_JSON_FILE_NAME,
                Constants.VALUE_TEXTURE_SCREEN_NAME
            )
            putString(Constants.EXTRA_SCREEN_TYPE, "TEXTURE")
            putString(Constants.KEY_LWP_NAME, lwpName)
        })
    }

    private fun navToTimer() {
        val intent = Intent(
            activity,
            Class.forName("com.sfaxdroid.timer.WallpaperSchedulerActivity")
        )
        startActivity(intent)
    }

    private fun openLwp(
        wallpaperObject: LwpItem,
    ) {
        when (wallpaperObject.type) {
            is LiveWallpaper.DouaLwp -> {
                navToTexture(Constants.KEY_WORD_IMG_LWP)
            }
            is LiveWallpaper.SkyView -> {
                Utils.openLiveWallpaper<SkyLiveWallpaper>(requireContext())
            }
            is LiveWallpaper.TimerLwp -> {
                navToTimer()
            }
            is LiveWallpaper.NameOfAllah2D -> {
                navToTexture(Constants.KEY_ANIM_2D_LWP)
            }
        }
    }

    private fun getItemType(screenType: ScreenType): Int {
        return when (screenType) {
            is ScreenType.Wall -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is ScreenType.CatWallpaper -> WallpapersListAdapter.TYPE_SQUARE_WALLPAPER
            is ScreenType.Cat -> WallpapersListAdapter.TYPE_CAT
            is ScreenType.Lab -> WallpapersListAdapter.TYPE_LAB
            else -> WallpapersListAdapter.TYPE_LWP
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
            //setHasFixedSize(true)
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
            HomeActivityNavBar.nbOpenAds++
            Intent(
                activity,
                Class.forName("com.sfaxdroid.detail.DetailsActivity")
            ).apply {
                putExtra(
                    Constants.EXTRA_IMG_URL,
                    getFullUrl(wallpaperObject.url)
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
                        val list = rep.wallpaperList.wallpapers.map { wall ->
                            WallpaperToLwpMapper().map(wall)
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
                        val list = rep.wallpaperList.wallpapers.map { wall ->
                            WallpaperToViewMapper().map(wall)
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
        when (val screenType = getType(screen)) {
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
                                val list = rep.wallpaperList.wallpapers.map { wall ->
                                    WallpaperToCategoryMapper().map(wall)
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
            ScreenType.CatWallpaper -> {
                GlobalScope.launch {
                    getCatWallpapersUseCase(GetCatWallpapersUseCase.Param(fileName))
                    {
                        when (it) {
                            is Response.SUCESS -> {
                                val rep =
                                    (it.response as Response.SUCESS).response as WallpaperResponse
                                val list = rep.wallpaperList.wallpapers.map { wall ->
                                    WallpaperToViewMapper().map(wall)
                                }
                                showContent(list, screenType)
                            }
                            is Response.FAILURE -> {

                            }
                        }
                    }
                }
            }
        }

    }

    private fun getType(type: String): ScreenType {
        return when (type) {
            "LWP" -> ScreenType.Lwp
            "CAT" -> ScreenType.Cat
            "LAB" -> ScreenType.Lab
            "TEXTURE" -> ScreenType.TEXTURE
            "TIMER" -> ScreenType.TIMER
            "CAT_WALL" -> ScreenType.CatWallpaper
            else -> ScreenType.Wall
        }
    }

    sealed class ScreenType {
        object Lwp : ScreenType()
        object Wall : ScreenType()
        object Cat : ScreenType()
        object Lab : ScreenType()
        object TEXTURE : ScreenType()
        object CatWallpaper : ScreenType()
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
        fun newInstance(
            fileName: String,
            screenType: String,
            lwpName: String? = null
        ): AllInOneFragment {
            return AllInOneFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.EXTRA_JSON_FILE_NAME, fileName)
                    putString(Constants.EXTRA_SCREEN_TYPE, screenType)
                    putString(Constants.KEY_LWP_NAME, lwpName)
                }
            }
        }
    }

}