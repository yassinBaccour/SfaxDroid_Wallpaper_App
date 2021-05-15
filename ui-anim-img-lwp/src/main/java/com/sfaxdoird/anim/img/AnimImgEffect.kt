package com.sfaxdoird.anim.img

import com.sfaxdroid.bases.UiEffect

internal sealed class AnimImgEffect : UiEffect {
    object GoToLiveWallpaper : AnimImgEffect()
}