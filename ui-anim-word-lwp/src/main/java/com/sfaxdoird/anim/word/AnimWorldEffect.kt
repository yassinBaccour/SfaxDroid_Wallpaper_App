package com.sfaxdoird.anim.word

import com.sfaxdroid.bases.UiEffect

sealed class AnimWorldEffect : UiEffect

object OpenLiveWallpaper : AnimWorldEffect()
object Retry:AnimWorldEffect()
