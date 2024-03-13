package com.sfaxdroid.base.utils

import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.mappers.TagView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class TagUtils {
    private fun mapTagNameToId() = mapOf(
        "Nature" to "3078326",
        "Cars" to "4361321",
        "Music" to "2912447",
        "Dogs" to "7694627",
        "Cats" to "7517522",
        "Buildings" to "5057263",
        "Travel" to "4888643"
    ).toMutableMap()

    private fun getTagImgUrl(
        tag: String, map: Map<String, String>, getUrl: suspend (String) -> String
    ): MutableStateFlow<String> {
        val url = MutableStateFlow("")
        CoroutineScope(Dispatchers.IO).launch {
            url.value = map[tag]?.let { getUrl(it) }.toString()
        }
        return url
    }

    fun provideMixedWallpaper(getUrl: suspend (String) -> String): List<PixaTagWithSearchData> {
        val tagNameToId = mapTagNameToId()
        val tagsWithSearchData = listOf(
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Mixed", tagNameToId) {
                    getUrl(it)
                }, tag = TagView("Mixed", "Mixed"), search = PixaSearch("", "", "200")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Music", tagNameToId) {
                    getUrl(it)
                },
                tag = TagView("Music", "Music"),
                search = PixaSearch("music+background", "music", "200")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Nature", tagNameToId) {
                    getUrl(it)
                },
                tag = TagView("Nature", "Nature"),
                search = PixaSearch("landscape", "nature", "200")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Cats", tagNameToId) {
                    getUrl(it)
                },
                tag = TagView("Cats", "Cats"),
                search = PixaSearch("domestic+cat", "animals", "200")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Cars", tagNameToId) {
                    getUrl(it)
                },
                tag = TagView("Cars", "Cars"),
                search = PixaSearch("sports+car", "transportation", "200")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Dogs", tagNameToId) {
                    getUrl(it)
                }, tag = TagView("Dogs", "Dogs"), search = PixaSearch("dogs", "animals", "200")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Buildings", tagNameToId) {
                    getUrl(it)
                },
                tag = TagView("Buildings", "Buildings"),
                search = PixaSearch("city", "buildings", "200")
            ),
            PixaTagWithSearchData(
                tagImgUrl = getTagImgUrl("Travel", tagNameToId) {
                    getUrl(it)
                },
                tag = TagView("Travel", "Travel"),
                search = PixaSearch("vacation", "travel", "200")
            ),
        )

        return tagsWithSearchData


    }

}