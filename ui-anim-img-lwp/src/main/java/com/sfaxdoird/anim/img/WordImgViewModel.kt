package com.sfaxdoird.anim.img

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sfaxdroid.app.ZipUtils
import com.sfaxdroid.base.*
import com.sfaxdroid.base.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
internal class WordImgViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fileManager: FileManager,
    private val deviceManager: DeviceManager,
    @Named("domain-url") var domainUrl: String,
    @ApplicationContext context: Context
) :
    ViewModel() {

    private var zipFile: File? = null
    private var zipDestination: File? = null
    private var backgroundFile: File? = null
    private var mDownloadId1 = 0
    private var mDownloadId2 = 0

    //TODO MVI add lazy
    var pref: SharedPrefsUtils? = SharedPrefsUtils(context)

    private val selectedColor = MutableStateFlow(0)
    private val progressValueFlow = MutableStateFlow<Pair<Int, Long>>(Pair(0, 0))
    private val progressInfoFlow = MutableStateFlow<ProgressionInfo>(ProgressionInfo.Idle)

    val state = combine(
        selectedColor,
        progressValueFlow,
        progressInfoFlow
    ) { color, progressValue, progressInfo ->
        AnimImgViewState(
            color = color,
            progressValue = progressValue,
            progressionInfo = progressInfo
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnimImgViewState.Empty
    )

    init {
        startDownload()
    }

    private fun startDownload() {
        val lwpFolder =
            fileManager.getTemporaryDirWithFolder(com.sfaxdoird.anim.img.Constants.KEY_LWP_FOLDER_CONTAINER)

        zipDestination = File(lwpFolder, Constants.ZIP_FOLDER_NAME)


        backgroundFile = File(lwpFolder, Constants.PNG_BACKFROUND_FILE_NAME)
        zipFile = File(lwpFolder, com.sfaxdoird.anim.img.Constants.PNG_ZIP_FILE_NAME)

        if (zipFile?.exists() == true) {
            backgroundFile?.delete()
        } else {

        }
    }

    private fun unzipAndDeleteZip() {
        ZipUtils.unzipFile(zipFile, zipDestination)
        if (zipFile?.exists() == true) {
            backgroundFile?.delete()
        }
    }

    override fun onCleared() {
        super.onCleared()
        zipFile = null
        zipDestination = null
        backgroundFile = null
    }

    fun saveColor(color: Int) {
        pref?.setSetting(com.sfaxdroid.bases.Constants.WALLPAPER_COLOR, color)
        viewModelScope.launch {
            selectedColor.emit(color)
        }
    }

}
