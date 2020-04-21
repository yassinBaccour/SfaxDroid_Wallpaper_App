package com.sami.rippel.feature.singleview

import android.app.Activity
import android.content.Intent
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sami.rippel.allah.R
import com.sami.rippel.base.BaseFragment
import com.sami.rippel.model.Constants
import com.sami.rippel.model.ViewModel
import com.sami.rippel.model.entity.StateEnum
import com.sami.rippel.model.entity.WallpaperObject
import com.sami.rippel.model.listner.OnStateChangeListener
import com.sami.rippel.presenter.AllWallpaperPresenter
import com.sami.rippel.presenter.Contract.WallpaperFragmentContract
import com.sami.rippel.ui.activity.DetailsActivity
import com.sami.rippel.ui.activity.GalleryActivity
import com.sami.rippel.ui.activity.HomeActivity
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

    private fun openWallpaper(wallpaperObject: WallpaperObject) {
        showDetailViewActivity(wallpaperObject)
    }

    private fun openCategory(wallpaperObject: WallpaperObject, carouselTypeEnum: CarouselTypeEnum) {
        when (carouselTypeEnum) {
            is CarouselTypeEnum.LIVEWALLPAPER -> {
            }
            is CarouselTypeEnum.CATEGORY -> {
                val categorName: String = wallpaperObject.name
                if (mListener != null) {
                    HomeActivity.nbOpenAds++
                    mListener.onOpenScreenTracker("CategoryGallery")
                    mListener.onTrackAction("CategoryOpen", categorName)
                }
                val intent = Intent(
                    activity,
                    GalleryActivity::class.java
                )
                intent.putParcelableArrayListExtra(
                    Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, mData
                )

                if (!categorName.isEmpty()) intent.putExtra(
                    Constants.DETAIL_IMAGE_POS,
                    categorName
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
                R.mipmap.ic_doua_lwp
            ), WallpaperObject(
                getString(R.string.TitleSkyboxLwp),
                getString(R.string.DescSkyboxLwp),
                resources.getColor(R.color.primary),
                R.mipmap.ic_skybox
            ), WallpaperObject(
                getString(R.string.TitleTimerLwp),
                getString(R.string.DescTimerLwp),
                resources.getColor(R.color.primary),
                R.mipmap.ic_timer_lwp
            ), WallpaperObject(
                getString(R.string.nameofallah2dtitle),
                getString(R.string.nameofallah2ddesc),
                resources.getColor(R.color.primary),
                R.mipmap.ic_nameofallah_lwp
            )
        )
        val lwp = CarouselView("Live Wallpaper 4K", lwpList, CarouselTypeEnum.LIVEWALLPAPER)
        listItem.add(6, ItemWrapperList(lwp, ArticleListAdapter.TYPE_CAROUSEL))

        val catList: List<WallpaperObject> = listOf(
            WallpaperObject(
                getString(R.string.TitleDouaLwp),
                getString(R.string.DescDouaLwp),
                resources.getColor(R.color.primary),
                R.mipmap.ic_doua_lwp
            ), WallpaperObject(
                getString(R.string.TitleSkyboxLwp),
                getString(R.string.DescSkyboxLwp),
                resources.getColor(R.color.primary),
                R.mipmap.ic_skybox
            ), WallpaperObject(
                getString(R.string.TitleTimerLwp),
                getString(R.string.DescTimerLwp),
                resources.getColor(R.color.primary),
                R.mipmap.ic_timer_lwp
            ), WallpaperObject(
                getString(R.string.nameofallah2dtitle),
                getString(R.string.nameofallah2ddesc),
                resources.getColor(R.color.primary),
                R.mipmap.ic_nameofallah_lwp
            )
        )
        val cat = CarouselView("All Category", catList, CarouselTypeEnum.CATEGORY)
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
                    getWallpaperList(mList),
                    { catItem -> openWallpaper(catItem) },
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
            HomeActivity.nbOpenAds++
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
                    0
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

    override fun getFragmentId(): Int {
        return R.layout.fragment_all_background
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

    companion object {
        fun newInstance(): AllInOneFragment {
            return AllInOneFragment()
        }
    }
}