package com.sfaxdroid.timer

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.extension.getFileName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WallpaperListFragment : Fragment() {

    private val viewModel: WallpaperListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_wallpaper_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        val wallpapersListAdapter = WallpapersListAdapter { url -> openDetail(url) }
        view.findViewById<RecyclerView>(R.id.recycler_view_wallpapers).apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = wallpapersListAdapter
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                it.apply {
                    wallpapersListAdapter.update(wallpaperList)
                    view.findViewById<ProgressBar>(R.id.progress_bar_list).visibility =
                        if (isRefresh) View.VISIBLE else View.GONE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uiEffects.collect {
                when (it) {
                    is WallpaperListEffects.DeleteImage -> {
                        showDeleteAlert(it.url)
                    }
                    is WallpaperListEffects.SaveImage -> {
                        showDownloadAlert(it.url)
                    }
                }
            }
        }
    }

    private fun initToolbar() {
        val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            title = getString(R.string.wallpaper_list)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun openDetail(url: String) {
        viewModel.submitAction(WallpaperListAction.ImageItemClick(url))
    }

    private fun showDeleteAlert(
        url: String
    ) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.alert_box_message_delete))
            .setPositiveButton(
                requireContext().getString(com.sfaxdroid.base.R.string.permission_accept_click_button)
            ) { _, _ ->
                viewModel.submitAction(WallpaperListAction.LoadFromStorage(url.getFileName()))
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
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            viewModel.submitAction(
                                WallpaperListAction.SaveBitmap(
                                    url.getFileName(),
                                    resource
                                )
                            )
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
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
