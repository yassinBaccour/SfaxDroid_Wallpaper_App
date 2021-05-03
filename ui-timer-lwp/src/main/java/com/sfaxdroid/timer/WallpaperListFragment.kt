package com.sfaxdroid.timer

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.extension.getFileName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wallpaper_list.*
import javax.inject.Inject

@AndroidEntryPoint
class WallpaperListFragment : Fragment() {

    private val viewModel: WallpaperListViewModel by viewModels()
    private var wallpapersListAdapter: WallpapersListAdapter? = null

    @Inject
    lateinit var fileManager: FileManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_wallpaper_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        wallpapersListAdapter = WallpapersListAdapter { url -> openDetail(url) }
        recycler_view_wallpapers?.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = wallpapersListAdapter
        }
        viewModel.wallpaperListLiveData.observe(viewLifecycleOwner) { list ->
            wallpapersListAdapter?.update(list)
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            title = getString(R.string.wallpaper_list)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun openDetail(url: String) {
        val exist = fileManager.getPermanentDirWithFile(url.getFileName()).exists()
        if (exist) showDeleteAlert(url) else showDownloadAlert(url)
    }

    private fun showDeleteAlert(
        url: String
    ) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.alert_box_message_delete))
            .setPositiveButton(
                requireContext().getString(com.sfaxdroid.base.R.string.permission_accept_click_button)
            ) { _, _ ->
                val info = fileManager.getTemporaryDirWithFile(url.getFileName())
                if (info.exists())
                    info.delete()
                viewModel.loadFromStorage()
            }
            .setNegativeButton(
                requireContext().getString(com.sfaxdroid.base.R.string.permission_deny_click_button)
            ) { _, _ ->
            }
            .create()
            .show()
    }

    private fun showDownloadAlert(
        url: String
    ) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.alert_box_message))
            .setPositiveButton(
                requireContext().getString(com.sfaxdroid.base.R.string.permission_accept_click_button)
            ) { _, _ ->
                Glide.with(requireContext()).asBitmap().load(url)
                    .into(object : SimpleTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            val isSaved = fileManager.saveBitmapToStorage(
                                resource,
                                url.getFileName(),
                                Constants.SAVE_PERMANENT,
                            )
                            resource.recycle()
                        }
                    })
            }
            .setNegativeButton(
                requireContext().getString(com.sfaxdroid.base.R.string.permission_deny_click_button)
            ) { _, _ ->
            }
            .create()
            .show()
    }

    companion object {
        fun newInstance(
            screenType: String,
        ): WallpaperListFragment {
            return WallpaperListFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.EXTRA_SCREEN_TYPE, screenType)
                }
            }
        }
    }
}