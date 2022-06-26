package com.sfaxdoird.anim.img

import com.sfaxdroid.bases.UiState
import javax.annotation.concurrent.Immutable

@Immutable
internal data class AnimImgViewState(
    val color: Int = -786456,
    val isOpenLwpButtonEnable: Boolean = false,
    val isCompleted: Boolean = false,
    val progressionInfo: ProgressionInfo = ProgressionInfo.Idle,
    val progressValue: Pair<Int, Long> = Pair(0, 0)
) : UiState {
    companion object {
        val Empty = AnimImgViewState()
    }
}

sealed class ProgressionInfo {
    object IdOneCompleted : ProgressionInfo()
    object IdTwoCompleted : ProgressionInfo()
    object Error : ProgressionInfo()
    object Idle : ProgressionInfo()
}