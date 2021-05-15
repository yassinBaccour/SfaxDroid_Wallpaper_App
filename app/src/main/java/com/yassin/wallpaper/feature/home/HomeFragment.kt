package com.yassin.wallpaper.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.data.entity.LiveWallpaper
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.sky.SkyLiveWallpaper
import com.yassin.wallpaper.R
import com.yassin.wallpaper.feature.home.adapter.TagAdapter
import com.yassin.wallpaper.feature.home.adapter.WallpapersListAdapter
import com.yassin.wallpaper.feature.other.PrivacyActivity
import com.yassin.wallpaper.utils.AppName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wallpapers.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    @Named("app-name")
    lateinit var appName: AppName

    private var wallpapersListAdapter: WallpapersListAdapter? = null

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_wallpapers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        lifecycleScope.launchWhenCreated {

            viewModel.uiState.collect {
                it.apply {
                    updateList(itemsList)
                    showFilter(tagList)
                    recycler_view_tag.visibility = if (isTagVisible) View.VISIBLE else View.GONE
                    progress_bar_wallpaper_list.visibility =
                        if (isRefresh) View.VISIBLE else View.GONE
                    toolbar.visibility = if (isToolBarVisible) View.VISIBLE else View.GONE
                    imgPrivacy.visibility = if (isPrivacyButtonVisible) View.VISIBLE else View.GONE
                    (activity as AppCompatActivity?)?.supportActionBar?.apply {
                        title = if (toolBarTitle.isNullOrEmpty()) requireContext().getString(
                            R.string.app_name
                        ) else toolBarTitle
                        setHomeButtonEnabled(true)
                        setDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnabled)
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEffects.collect {
                when (it) {
                    is OpenCategory -> {
                        HomeActivityNavBar.nbOpenAds++
                        findNavController().navigate(
                            R.id.category_show_navigation_fg,
                            Bundle().apply {
                                putString(Constants.EXTRA_JSON_FILE_NAME, it.wallpaperObject.file)
                                putString(Constants.EXTRA_SCREEN_NAME, it.wallpaperObject.name)
                                putString(Constants.EXTRA_SCREEN_TYPE, "CAT_WALL")
                            }
                        )
                    }
                    is OpenLiveWallpaper -> openLwp(it.wallpaperObject)
                    is OpenWallpaperByType -> openByType(
                        it.simpleWallpaperView,
                        it.parentName,
                        it.screenName
                    )
                }
            }
        }
    }

    private fun initView() {
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)

        wallpapersListAdapter = WallpapersListAdapter(
            arrayListOf()
        ) { catItem -> openWallpaper(catItem) }

        recycler_view_wallpapers?.apply {
            layoutManager = getLwpLayoutManager()
            adapter = wallpapersListAdapter
            addOnItemTouchListener(
                object : RecyclerView.OnItemTouchListener {
                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                    override fun onInterceptTouchEvent(
                        recyclerView: RecyclerView,
                        motionEvent: MotionEvent
                    ): Boolean {
                        if (motionEvent.action == MotionEvent.ACTION_DOWN &&
                            recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING
                        ) {
                            recyclerView.stopScroll()
                        }
                        return false
                    }

                    override fun onRequestDisallowInterceptTouchEvent(
                        disallowIntercept: Boolean
                    ) {
                    }
                })
        }

        imgPrivacy?.setOnClickListener {
            context?.startActivity(
                Intent(
                    context,
                    PrivacyActivity::class.java
                )
            )
        }
    }

    private fun showFilter(tagList: List<TagView>) {
        recycler_view_tag.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = TagAdapter(
                tagList
            ) { viewModel.submitAction(WallpaperListAction.LoadTags(it)) }
        }
    }

    private fun updateList(list: List<ItemWrapperList<Any>>) {
        wallpapersListAdapter?.update(list)
    }

    private fun getFullUrl(url: String): String {
        return url.replace("_preview", "")
    }

    private fun openByType(
        wallpaperObject: SimpleWallpaperView,
        selectedLwpName: String,
        screenName: String
    ) {
        when (selectedLwpName) {
            Constants.KEY_WORD_IMG_LWP -> {
                findNavController().navigate(
                    R.id.navigate_to_word_img,
                    Bundle().apply {
                        putString(
                            Constants.EXTRA_URL_TO_DOWNLOAD,
                            getFullUrl(wallpaperObject.detailUrl)
                        )
                        putString(
                            Constants.EXTRA_SCREEN_NAME,
                            screenName
                        )
                    }
                )
            }
            Constants.KEY_ANIM_2D_LWP -> {
                findNavController().navigate(
                    R.id.navigate_to_anim_2d,
                    Bundle().apply {
                        putString(
                            Constants.EXTRA_URL_TO_DOWNLOAD,
                            getFullUrl(wallpaperObject.detailUrl)
                        )
                        putString(
                            Constants.EXTRA_SCREEN_NAME,
                            screenName
                        )
                    }
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
        viewModel.submitAction(WallpaperListAction.OpenItems(wallpaperObject))
    }

    private fun openLwp(wallpaperObject: LwpItem) {
        when (wallpaperObject.type) {
            is LiveWallpaper.WordImg -> {
                navToTexture(Constants.KEY_WORD_IMG_LWP, wallpaperObject.name)
            }
            is LiveWallpaper.SkyView -> {
                Utils.openLiveWallpaper<SkyLiveWallpaper>(requireContext())
            }
            is LiveWallpaper.TimerLwp -> {
                navToTimer(wallpaperObject.name)
            }
            is LiveWallpaper.Word2d -> {
                navToTexture(Constants.KEY_ANIM_2D_LWP, wallpaperObject.name)
            }
        }
    }

    private fun navToTimer(screeName: String) {
        findNavController().navigate(
            R.id.navigate_to_timer,
            Bundle().apply { putString(Constants.EXTRA_SCREEN_NAME, screeName) }
        )
    }

    private fun navToTexture(lwpName: String, screeName: String) {
        findNavController().navigate(
            R.id.chooser_navigation_fg,
            Bundle().apply {
                putString(
                    Constants.EXTRA_JSON_FILE_NAME,
                    Constants.VALUE_TEXTURE_JSON_FILE_NAME
                )
                putString(Constants.EXTRA_SCREEN_TYPE, Constants.VALUE_TEXTURE_SCREEN_NAME)
                putString(Constants.KEY_LWP_NAME, lwpName)
                putString(Constants.EXTRA_SCREEN_NAME, screeName)
            }
        )
    }

    private fun showDetailViewActivity(wallpaperObject: SimpleWallpaperView, lwpName: String = "") {
        HomeActivityNavBar.nbOpenAds++
        findNavController().navigate(
            R.id.navigate_to_details,
            Bundle().apply {
                putString(
                    Constants.EXTRA_IMG_URL,
                    getFullUrl(wallpaperObject.detailUrl)
                )
                if (lwpName.isNotEmpty()) {
                    putString(Constants.KEY_LWP_NAME, lwpName)
                }
            }
        )
    }

    private fun getLwpLayoutManager(): LayoutManager {
        return GridLayoutManager(
            context,
            if (appName == AppName.SfaxDroid) 3 else 4
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
}
