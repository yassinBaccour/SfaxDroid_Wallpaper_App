package com.sfaxdoird.anim.img

import com.sfaxdroid.bases.UiEvent

internal sealed class AnimImgAction : UiEvent {
    object OpenLiveWallpaper : AnimImgAction()
    data class ChangeColor(var color: Int) : AnimImgAction()
}
