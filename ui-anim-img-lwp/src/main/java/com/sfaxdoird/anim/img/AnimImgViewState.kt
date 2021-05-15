package com.sfaxdoird.anim.img

import com.sfaxdroid.bases.UiState
import javax.annotation.concurrent.Immutable

@Immutable
internal data class AnimImgViewState(
    val color: Int = -786456,
    val isOpenLwpButtonEnable: Boolean = false,
    val isCompleted: Boolean = false,
    val progressionInfo: ProgressionInfo = ProgressionInfo.Idle
) : UiState

sealed class ProgressionInfo {
    object IdOneCompleted : ProgressionInfo()
    object IdTwoCompleted : ProgressionInfo()
    object Idle : ProgressionInfo()
}