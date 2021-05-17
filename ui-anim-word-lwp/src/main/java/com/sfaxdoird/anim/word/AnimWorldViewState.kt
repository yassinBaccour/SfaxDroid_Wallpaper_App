package com.sfaxdoird.anim.word

import com.sfaxdroid.bases.UiState
import javax.annotation.concurrent.Immutable

@Immutable
data class AnimWorldViewState(
    val color: Int = -786456,
    val isOpenLwpButtonEnable: Boolean = false,
    val isCompleted: Boolean = false,
    val size: Int = 2,
    val style: Int = 1,
) : UiState
