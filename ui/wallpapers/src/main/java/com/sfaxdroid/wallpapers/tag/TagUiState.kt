package com.sfaxdroid.wallpapers.tag

import com.sfaxdroid.wallpapers.core.GroupUiModel

internal sealed class TagUiState {
    data class Success(val sections: List<GroupUiModel>) : TagUiState()
    data object Failure : TagUiState()
    data object Loading : TagUiState()
}
