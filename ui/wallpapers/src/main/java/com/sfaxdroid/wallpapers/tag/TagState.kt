package com.sfaxdroid.wallpapers.tag

import com.sfaxdroid.wallpapers.core.GroupUiModel

internal sealed class TagState {
    data class Success(
        val title: String,
        val sections: List<GroupUiModel>,
        val loadFromPartner: Boolean,
        val tags: Pair<String, String>
    ) : TagState()

    data object Failure : TagState()
    data object Loading : TagState()
}


