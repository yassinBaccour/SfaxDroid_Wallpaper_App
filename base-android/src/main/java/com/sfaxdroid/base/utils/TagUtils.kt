package com.sfaxdroid.base.utils

class TagUtils {
    val tagNameToId = mutableMapOf<String, String>()
    private fun mapTagNameToId() {
        tagNameToId["Nature"] = "3078326"
        tagNameToId["Cars"] = "4361321"
        tagNameToId["Music"] = "2912447"
        tagNameToId["Dogs"] = "7694627"
        tagNameToId["Cats"] = "7517522"
        tagNameToId["Buildings"] = "5057263"
        tagNameToId["Travel"] = "4888643"
    }
    init {
        mapTagNameToId()
    }
}