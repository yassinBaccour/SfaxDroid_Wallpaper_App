package com.sfaxdroid.timer

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import com.sfaxdroid.base.FileUtils.Companion.getPermanentDirListFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class RetrieveWallpaperService : JobService() {

    private val sharedPref: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )

    override fun onStartJob(params: JobParameters): Boolean {
        GlobalScope.launch(Dispatchers.Default) {
            val nbFile = getPermanentDirListFiles(
                baseContext,
                baseContext.getString(R.string.app_namenospace)
            )?.size ?: 0
            var currentWallpaper = sharedPref.getInt("currentWallpaper", 0)
            if (nbFile > 0) {
                if (currentWallpaper >= nbFile) {
                    updateSchedulerSettings(0)
                    currentWallpaper = 0
                }
                Utils.setWallpaperFromFile(
                    baseContext,
                    getString(R.string.app_namenospace),
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
        val sharedPref =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = sharedPref.edit()
        editor.putInt("currentWallpaper", currentWallpaper)
        editor.apply()
    }
}