package com.sfaxdroid.detail.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.commion.ui.compose.Destination
import com.sfaxdroid.detail.ui.WallpaperUtils.isColorDark
import com.sfaxdroid.details.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = WallpaperDetailViewModel.Factory::class)
internal class WallpaperDetailViewModel @AssistedInject constructor(@Assisted val navKey: Destination.Detail) :
    ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Destination.Detail): WallpaperDetailViewModel
    }

    private val eventsChannel = Channel<WallpaperDetailEvent>(UNLIMITED)
    internal val events = eventsChannel.receiveAsFlow()

    private val wallpaperDetailState = MutableStateFlow(
        WallpaperDetailUiState(
            url = navKey.url,
            tag = navKey.tag,
            source = navKey.source
        )
    )
    val state = wallpaperDetailState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = WallpaperDetailUiState()
    )

    internal fun addBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            val isDark = isColorDark(bitmap)
            wallpaperDetailState.update { current ->
                current.copy(wallpaper = bitmap, isImageDark = isDark, isWallpaperLoading = false)
            }
        }
    }

    internal fun setAsWallpaper(context: Context) {
        viewModelScope.launch {
            setLoadingState(true)
            try {
                withContext(Dispatchers.IO) {
                    WallpaperUtils.setWallpaperWithChooser(context, wallpaperDetailState.value.wallpaper!!)
                }
            } finally {
                eventsChannel.trySend(WallpaperDetailEvent.ShowMessage(R.string.wallpaper_set_successfully))
                setLoadingState(false)
            }
        }
    }

    internal fun openTag(tag: String) {
        eventsChannel.trySend(
            WallpaperDetailEvent.NavigateToDestination(
                Destination.Tag(
                    title = tag,
                    tag = Pair(tag, ""),
                    loadFromPartner = true
                )
            )
        )
    }

    private fun setLoadingState(isLoading: Boolean) = wallpaperDetailState.update { current ->
        current.copy(isWallpaperLoading = isLoading)
    }

}