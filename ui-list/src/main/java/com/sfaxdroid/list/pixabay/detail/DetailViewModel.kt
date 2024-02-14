package com.sfaxdroid.list.pixabay.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.detail.ActionTypeEnum
import com.sfaxdroid.detail.utils.DetailUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    private val fileManager: FileManager,
) : ViewModel() {
    internal fun setAsWallpaper(url: String, context: Context) {
        DetailUtils.saveToFileToTempsDirAndChooseAction(
            url = url,
            action = ActionTypeEnum.SetAsWallpaper,
            context = context,
            fileManager = fileManager
        ) { isSaved, action ->
        }
    }
}