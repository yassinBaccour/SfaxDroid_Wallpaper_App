package com.sfaxdoird.anim.word

import android.widget.Button
import android.widget.TextView
import com.sfaxdroid.bases.UiEvent

internal sealed class AnimWorldAction : UiEvent {
    data class ChangeColor(var color: Int) : AnimWorldAction()
    data class ChangeSize(var size: Int, var button: Button) : AnimWorldAction()
    data class ChangeFont(var style: Int, var textView: TextView) : AnimWorldAction()
    object DownloadData : AnimWorldAction()
    object OpenLiveWallpaper : AnimWorldAction()
}
