package com.yassin.wallpaper.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.yassin.wallpaper.R
import com.yassin.wallpaper.feature.home.adapter.WallpapersListAdapter
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.data.entity.LiveWallpaper
import com.sfaxdroid.data.mappers.*
import com.sfaxdroid.sky.SkyLiveWallpaper
import com.yassin.wallpaper.feature.home.adapter.TagAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wallpapers.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var wallpapersListAdapter: WallpapersListAdapter? = null

    private val selectedLwpName by lazy {
        arguments?.getString(Constants.KEY_LWP_NAME).orEmpty()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallpapers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wallpaperListLiveData.observe(viewLifecycleOwner) { list ->
            showContent(list)
        }
        viewModel.tagVisibility.observe(viewLifecycleOwner) { visibility ->
            tagVisibility(visibility)
        }
    }

    private fun getFullUrl(url: String): String {
        return url.replace("_preview", "")
    }

    private fun openByType(wallpaperObject: SimpleWallpaperView) {
        when (selectedLwpName) {
            Constants.KEY_WORD_IMG_LWP -> {
                findNavController().navigate(R.id.navigate_to_word_img,
                    Bundle().apply {
                        putString(
                            Constants.EXTRA_URL_TO_DOWNLOAD,
                            getFullUrl(wallpaperObject.detailUrl)
                        )
                    })
            }
            Constants.KEY_ANIM_2D_LWP -> {
                findNavController().navigate(R.id.navigate_to_anim_2d,
                    Bundle().apply {
                        putString(
                            Constants.EXTRA_URL_TO_DOWNLOAD,
                            getFullUrl(wallpaperObject.detailUrl)
                        )
                    })
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
                HomeActivityNavBar.nbOpenAds++
                findNavController().navigate(R.id.category_show_navigation_fg,
                    Bundle().apply {
                        putString(Constants.EXTRA_JSON_FILE_NAME, wallpaperObject.file)
                        putString(Constants.EXTRA_SCREEN_TYPE, "CAT_WALL")
                    })
            }
        }
    }

    private fun navToTimer() {
        findNavController().navigate(R.id.navigate_to_timer)
    }

    private fun navToTexture(lwpName: String) {
        findNavController().navigate(R.id.chooser_navigation_fg, Bundle().apply {
            putString(
                Constants.EXTRA_JSON_FILE_NAME,
                Constants.VALUE_TEXTURE_JSON_FILE_NAME
            )
            putString(Constants.EXTRA_SCREEN_TYPE, Constants.VALUE_TEXTURE_SCREEN_NAME)
            putString(Constants.KEY_LWP_NAME, lwpName)
        })
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

    private fun showContent(mList: List<ItemWrapperList<Any>>) {
        wallpapersListAdapter = WallpapersListAdapter(
            mList
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

        recycler_view_tag.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = TagAdapter(
                listOf(
                    TagView("New"),
                    TagView("Arabic"),
                    TagView("Top Arabic"),
                    TagView("Doua"),
                    TagView("Abusaeed"),
                    TagView("m5015")
                ),
                ::onTagClickListener
            )
        }
    }

    private fun onTagClickListener(tagView: TagView) {

    }

    private fun showDetailViewActivity(wallpaperObject: SimpleWallpaperView, lwpName: String = "") {
        HomeActivityNavBar.nbOpenAds++
        findNavController().navigate(R.id.navigate_to_details, Bundle().apply {
            putString(
                Constants.EXTRA_IMG_URL,
                getFullUrl(wallpaperObject.detailUrl)
            )
            if (lwpName.isNotEmpty()) {
                putString(Constants.KEY_LWP_NAME, lwpName)
            }
        })
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

    private fun tagVisibility(visibility: Boolean) {
        recycler_view_tag.visibility = if (visibility) View.VISIBLE else View.GONE
    }


    companion object {
        fun newInstance(
            fileName: String,
            screenType: String,
            lwpName: String? = null
        ): HomeFragment {
            return HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.EXTRA_JSON_FILE_NAME, fileName)
                    putString(Constants.EXTRA_SCREEN_TYPE, screenType)
                    putString(Constants.KEY_LWP_NAME, lwpName)
                }
            }
        }
    }

}