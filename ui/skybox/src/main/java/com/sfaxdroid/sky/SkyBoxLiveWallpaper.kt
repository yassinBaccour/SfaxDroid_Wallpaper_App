package com.sfaxdroid.sky

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.sfaxdroid.skybox.R
import org.rajawali3d.view.ISurface
import org.rajawali3d.wallpaper.Wallpaper

class SkyBoxLiveWallpaper : Wallpaper() {

    override fun onCreateEngine(): Engine {
        return try {
            if (!checkOpenGLSupport()) {
                showCompatibilityError()
                createFallbackEngine()
            } else {
                WallpaperEngine(
                    baseContext,
                    SkyBoxRenderer(this, R.drawable.ic_skybox),
                    ISurface.ANTI_ALIASING_CONFIG.NONE
                )
            }
        } catch (e: Exception) {
            handleEngineCreationError(e)
            createFallbackEngine()
        }
    }


    private fun showCompatibilityError() {
        Toast.makeText(
            this,
            getString(R.string.error_not_supported_lwp),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun checkOpenGLSupport() = try {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        val configInfo = activityManager.deviceConfigurationInfo
        configInfo.reqGlEsVersion >= 0x20000
    } catch (_: Exception) {
        false
    }

    private fun handleEngineCreationError(exception: Exception) {
        val errorMessage = getOpenGlError(exception)
        Log.e(TAG, errorMessage, exception)
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun getOpenGlError(exception: Exception) = when {
        exception.message?.contains("OpenGL", ignoreCase = true) == true ->
            "OpenGL not supported on this device"

        exception.message?.contains("Rajawali", ignoreCase = true) == true ->
            "Rendering engine initialization failed"

        else -> "Wallpaper cannot run on this device: ${exception.message}"
    }

    private fun createFallbackEngine(): Engine {
        return object : Engine() {
            override fun onCreate(surfaceHolder: android.view.SurfaceHolder?) {
                super.onCreate(surfaceHolder)
                try {
                    surfaceHolder?.let { holder ->
                        val canvas = holder.lockCanvas()
                        canvas?.let {
                            it.drawColor(android.graphics.Color.BLACK)
                            holder.unlockCanvasAndPost(it)
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
    }
}
