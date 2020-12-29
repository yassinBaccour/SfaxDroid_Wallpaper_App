package com.sami.rippel.feature.main.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sami.rippel.allah.R
import com.sami.rippel.base.BaseFragment
import com.sami.rippel.feature.main.activity.HomeActivity
import com.sami.rippel.feature.main.presenter.AllWallpaperPresenter
import com.sami.rippel.feature.main.presenter.Contract.WallpaperFragmentContract
import com.sami.rippel.model.ViewModel
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.DeviceUtils.Companion.isConnected
import com.sfaxdroid.base.RecyclerItemClickListener
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.bases.OnStateChangeListener
import com.sfaxdroid.bases.StateEnum
import com.sfaxdroid.bases.TypeCellItemEnum
import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import com.sfaxdroid.domain.GetAllWallpapersUseCase
import com.sfaxdroid.gallery.GalleryAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class AllBackgroundFragment : BaseFragment<AllWallpaperPresenter?>(),
    WallpaperFragmentContract.View, OnStateChangeListener, HasAndroidInjector {
    private var mFragment: AllBackgroundFragment? = null

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var repo: GetAllWallpapersUseCase

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModel.Current.registerOnStateChangeListener(this)
        mFragment = this

        GlobalScope.launch {
            repo(GetAllWallpapersUseCase.Param("new.json"))
            {
                when (it) {
                    is Response.SUCESS -> {
                        Log.d("....", it.response.toString())
                    }
                    is Response.FAILURE -> {

                    }
                }
            }
        }
    }

    override fun getFragmentActivity(): Activity {
        return activity!!
    }

    override fun fillForm() {}
    override fun instantiatePresenter(): AllWallpaperPresenter {
        return AllWallpaperPresenter() //FIXME VIEW MODEL NULL
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ViewModel.Current.unregisterOnStateChangeListener(this)
    }

    override fun initEventAndData() {
        mPresenter!!.getWallpaper()
    }

    override fun showContent(mList: List<WallpaperObject>) {
        if (ViewModel.Current.isWallpapersLoaded) {
            //Fixme retrolabda
            //WallpaperCategory wallpaperCategory = ViewModel.Current.retrofitWallpObject.getCategoryList().stream().filter(x -> x.getTitle().equals("All")).findFirst().orElse(null);
            mData.clear()
            mData = ArrayList(mList)
            if (activity != null && isConnected(activity!!)!! && mData != null && mData.size > 0) {
                val mAdapter = GalleryAdapter(activity, mData, TypeCellItemEnum.GALLERY_CELL)
                mRecyclerView.adapter = mAdapter
                mRecyclerView
                    .addOnItemTouchListener(RecyclerItemClickListener(
                        activity
                    ) { view: View?, position: Int ->
                        if (mFragment!!.view != null && position >= 0) {
                            HomeActivity.nbOpenAds++
                            var intent: Intent? = null
                            try {
                                intent = Intent(
                                    activity,
                                    Class.forName("com.sfaxdroid.detail.DetailsActivity")
                                )
                            } catch (e: ClassNotFoundException) {
                                e.printStackTrace()
                            }
                            intent!!.putParcelableArrayListExtra(
                                Constants.LIST_FILE_TO_SEND_TO_DETAIL_VIEW_PAGER, mData
                            )
                            intent.putExtra(Constants.DETAIL_IMAGE_POS, position)
                            startActivity(intent)
                        }
                    })
            } else {
                if (mFragment!!.view != null) {
                    Toast.makeText(
                        activity, getString(R.string.NoConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override val fragmentId: Int
        get() = R.layout.fragment_all_background

    override fun getLayoutManager(): LayoutManager {
        return GridLayoutManager(
            fragmentActivity, 3
        )
    }

    override fun onStateChange(state: StateEnum) {
        mPresenter?.getWallpaper()
    }

    override fun showSnackMsg(msg: String) {}
    override fun showLoading() {}
    override fun hideLoading() {}

    companion object {
        fun newInstance(): AllBackgroundFragment {
            return AllBackgroundFragment()
        }
    }
}