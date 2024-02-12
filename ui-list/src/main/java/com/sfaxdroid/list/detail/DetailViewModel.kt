package com.sfaxdroid.list.detail

import androidx.lifecycle.ViewModel
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val fileManager: FileManager
) :ViewModel(){

}