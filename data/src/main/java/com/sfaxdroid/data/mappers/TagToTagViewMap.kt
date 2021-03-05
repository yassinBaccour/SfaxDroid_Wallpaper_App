package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.Tag

class TagToTagViewMap : SfaxDroidMapper<Tag, TagView> {
    override fun map(from: Tag?, isSmallScreen: Boolean): TagView {
        return TagView(from?.title.orEmpty(), from?.fileName.orEmpty())
    }
}