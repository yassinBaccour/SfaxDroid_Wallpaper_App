package com.sfaxdroid.timer

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import com.sfaxdroid.base.DeviceManager
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.bases.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@AndroidEntryPoint
internal class RetrieveWallpaperService : JobService() {

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var deviceManager: DeviceManager

    override fun onStartJob(params: JobParameters): Boolean {
        val sharedPref: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(
                baseContext
            )
        GlobalScope.launch(Dispatchers.Default) {
            val nbFile = fileManager.getPermanentDirListFiles().size
            var currentWallpaper = sharedPref.getInt(Constants.CURRENT_WALLPAPER_KEY, 0)
            if (nbFile > 0) {
                if (currentWallpaper >= nbFile) {
                    updateSchedulerSettings(0)
                    currentWallpaper = 0
                }
                TimerUtils.setWallpaperFromFile(
                    baseContext,
                    fileManager.getPermanentDirListFiles(),
                    deviceManager.getScreenWidthPixels(),
                    deviceManager.getScreenHeightPixels(),
                    currentWallpaper
                )
                currentWallpaper += 1
                updateSchedulerSettings(currentWallpaper)
            }
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters) = false

    private fun updateSchedulerSettings(currentWallpaper: Int) {
        PreferenceManager.getDefaultSharedPreferences(baseContext).edit().apply {
            putInt(Constants.CURRENT_WALLPAPER_KEY, currentWallpaper)
            apply()
        }
    }
}
