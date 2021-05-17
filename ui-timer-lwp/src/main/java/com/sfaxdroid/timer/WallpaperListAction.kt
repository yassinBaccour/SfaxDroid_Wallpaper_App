package com.sfaxdroid.timer

import android.graphics.Bitmap
import com.sfaxdroid.bases.UiEvent

sealed class WallpaperListAction : UiEvent {

    data class LoadFromStorage(var fileName: String) : WallpaperListAction()
    data class SaveBitmap(var fileName: String, var bitmap: Bitmap) : WallpaperListAction()
    data class ImageItemClick(var url: String) : WallpaperListAction()
}
