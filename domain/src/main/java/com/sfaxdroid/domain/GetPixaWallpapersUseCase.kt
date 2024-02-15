package com.sfaxdroid.domain

import com.sfaxdroid.data.entity.PixaTagWithSearchData
import com.sfaxdroid.data.mappers.PixaItem
import com.sfaxdroid.data.repositories.ApiService
import javax.inject.Inject
import javax.inject.Named

const val pixaBayImageType = "photo"
const val pixaBayPerPage = "200"
const val pixaBaySafeSearch = "true"

class GetPixaWallpapersUseCase
@Inject
constructor(@Named("pixabay-key") private val pixaBayApiKey: String) {
    suspend fun getResult(pixaTagWithSearchDataList: List<PixaTagWithSearchData>): List<PixaItem> {
        var ret = mutableListOf<PixaItem>()
        pixaTagWithSearchDataList.forEach { s ->
            val response =
                ApiService.getInstance()
                    .getImages(
                        apiKey = pixaBayApiKey,
                        category = s.search.category,
                        imageType = pixaBayImageType,
                        perPage = pixaBayPerPage,
                        searchTerm = s.search.searchTerm,
                        safeSearch = pixaBaySafeSearch
                    )
            ret.addAll(response.hits)
        }
        return ret
    }
}
