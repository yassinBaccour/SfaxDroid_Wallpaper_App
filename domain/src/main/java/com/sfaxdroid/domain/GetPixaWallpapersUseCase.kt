package com.sfaxdroid.domain

import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.repositories.ApiService
import javax.inject.Inject
import javax.inject.Named

const val pixaBayImageType = "photo"
const val pixaBaySafeSearch = "true"
const val pixaBayPage = "1"

class GetPixaWallpapersUseCase
@Inject
constructor(@Named("pixabay-key") private val pixaBayApiKey: String) {
    suspend fun getResult(search: PixaSearch) =
        ApiService.getInstance()
            .getImages(
                apiKey = pixaBayApiKey,
                category = search.category,
                imageType = pixaBayImageType,
                perPage = search.perPage,
                searchTerm = search.searchTerm,
                safeSearch = pixaBaySafeSearch,
                page = pixaBayPage
            )
            .hits

    suspend fun getUrl(id: String) =
        ApiService.getInstance()
            .getUrl(apiKey = pixaBayApiKey, id = id)
            .hits[0]
            .webformatURL
}