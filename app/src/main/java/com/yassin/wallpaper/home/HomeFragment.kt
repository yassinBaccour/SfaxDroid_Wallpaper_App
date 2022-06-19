package com.yassin.wallpaper.home

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.extension.getFullUrl
import com.sfaxdroid.base.utils.Utils
import com.sfaxdroid.data.entity.LiveWallpaper
import com.sfaxdroid.data.mappers.BaseWallpaperView
import com.sfaxdroid.data.mappers.LwpItem
import com.sfaxdroid.data.mappers.SimpleWallpaperView
import com.sfaxdroid.data.mappers.TagView
import com.sfaxdroid.sky.SkyLiveWallpaper
import com.yassin.wallpaper.R
import com.yassin.wallpaper.databinding.FragmentWallpapersBinding
import com.sfaxdroid.list.adapter.TagAdapter
import com.sfaxdroid.list.adapter.WallpapersListAdapter
import com.sfaxdroid.data.entity.AppName
import com.sfaxdroid.data.mappers.ItemWrapperList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_wallpapers) {

    @Inject
    @Named("app-name")
    lateinit var appName: AppName

    private var wallpapersListAdapter: WallpapersListAdapter? = null
    private var wallpapersTagAdapter: TagAdapter? = null

    private val viewModel: com.sfaxdroid.list.WallpaperListViewModel by viewModels()
    private lateinit var binding: FragmentWallpapersBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWallpapersBinding.bind(view)
        initView()
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                it.apply {
                    binding.recyclerViewTag.visibility =
                        if (isTagVisible) View.VISIBLE else View.GONE
                    binding.progressBarWallpaperList.visibility =
                        if (isRefresh) View.VISIBLE else View.GONE
                    binding.toolbar.visibility = if (isToolBarVisible) View.VISIBLE else View.GONE
                    binding.imgPrivacy.visibility =
                        if (isPrivacyButtonVisible) View.VISIBLE else View.GONE
                    (activity as AppCompatActivity?)?.supportActionBar?.apply {
                        title = if (toolBarTitle.isEmpty()) requireContext().getString(
                            R.string.app_name
                        ) else toolBarTitle
                        setHomeButtonEnabled(true)
                        setDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnabled)
                    }
                    updateList(itemsList)
                    showFilter(tagList)
                    setTagListSelectedItem(tagSelectedPosition)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEffects.collect {
                when (it) {
                    is com.sfaxdroid.list.OpenCategory -> navigateToCategory(it)
                    is com.sfaxdroid.list.OpenLiveWallpaper -> openLwp(it.wallpaperObject)
                    is com.sfaxdroid.list.OpenWallpaperByType -> openByType(
                        it.simpleWallpaperView,
                        it.parentName,
                        it.screenName
                    )
                }
            }
        }
    }

    private fun setTagListSelectedItem(position: Int) {
        wallpapersTagAdapter?.setSelectedPosition(position)
    }

    private fun initView() {
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)

        wallpapersListAdapter = WallpapersListAdapter(
            arrayListOf()
        ) { catItem -> openWallpaper(catItem) }

        binding.recyclerViewWallpapers.apply {
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
        wallpapersTagAdapter = TagAdapter(
            arrayListOf(), ::setSelectedPositionItem
        ) { viewModel.submitAction(com.sfaxdroid.list.WallpaperListAction.LoadTags(it)) }
        binding.recyclerViewTag.adapter = wallpapersTagAdapter

        binding.imgPrivacy.setOnClickListener {
            context?.startActivity(
                Intent(
                    context,
                    PrivacyActivity::class.java
                )
            )
        }
    }

    private fun setSelectedPositionItem(position: Int) {
        viewModel.submitAction(
            com.sfaxdroid.list.WallpaperListAction.UpdateTagSelectedPosition(
                position
            )
        )
        binding.recyclerViewWallpapers.scrollToPosition(0)
    }

    private fun showFilter(tagList: List<TagView>) {
        wallpapersTagAdapter?.update(tagList)
    }

    private fun updateList(list: List<ItemWrapperList>) {
        wallpapersListAdapter?.update(list)
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
                            wallpaperObject.detailUrl.getFullUrl()
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
                            wallpaperObject.detailUrl.getFullUrl()
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
        viewModel.submitAction(com.sfaxdroid.list.WallpaperListAction.OpenItems(wallpaperObject))
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
            else -> {}
        }
    }

    private fun navToTimer(screeName: String) {
        findNavController().navigate(
            R.id.navigate_to_timer,
            Bundle().apply { putString(Constants.EXTRA_SCREEN_NAME, screeName) }
        )
    }

    private fun navigateToCategory(openCategory: com.sfaxdroid.list.OpenCategory) {
        findNavController().navigate(
            R.id.category_show_navigation_fg,
            Bundle().apply {
                putString(Constants.EXTRA_JSON_FILE_NAME, openCategory.wallpaperObject.file)
                putString(Constants.EXTRA_SCREEN_NAME, openCategory.wallpaperObject.name)
                putString(Constants.EXTRA_SCREEN_TYPE, "CAT_WALL")
            }
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
        findNavController().navigate(
            R.id.navigate_to_details,
            Bundle().apply {
                putString(
                    Constants.EXTRA_IMG_URL,
                    wallpaperObject.detailUrl.getFullUrl()
                )
                putBoolean(
                    Constants.KEY_IS_FULL_SCREEN,
                    appName == AppName.AccountTwo
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
}
