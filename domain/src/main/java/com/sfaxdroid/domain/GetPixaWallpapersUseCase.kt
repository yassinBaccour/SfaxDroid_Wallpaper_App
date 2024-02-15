package com.sfaxdroid.domain

import com.sfaxdroid.data.entity.PixaSearch
import com.sfaxdroid.data.mappers.PixaItem
import com.sfaxdroid.data.repositories.ApiService
import javax.inject.Inject
import javax.inject.Named

const val pixaBaySearchTermNature = "landscape"
const val pixaBaySearchTermCars = "cars"
const val pixaBayImageType = "photo"
const val pixaBayPerPage = "200"
const val pixaBayCategoryNature = "nature"
const val pixaBayCategoryCars = "transportation"
const val pixaBaySafeSearch = "true"

class GetPixaWallpapersUseCase
@Inject constructor(
    @Named("pixabay-key") private val pixaBayApiKey: String
) {
    suspend fun getResult(pixaSearch: PixaSearch? = null): List<PixaItem> {
        var ret = mutableListOf<PixaItem>()
        if (pixaSearch == null){
            ret+= ApiService.getInstance().getImages(
                pixaBayApiKey,
                pixaBaySearchTermNature,
                pixaBayImageType,
                pixaBayPerPage,
                pixaBayCategoryNature,
                pixaBaySafeSearch
            ).hits

            ret+= ApiService.getInstance().getImages(
                pixaBayApiKey,
                pixaBaySearchTermCars,
                pixaBayImageType,
                pixaBayPerPage,
                pixaBayCategoryCars,
                pixaBaySafeSearch
            ).hits

        }else{
            ret = ApiService.getInstance().getImages(
                pixaBayApiKey,
                pixaSearch.searchTerm,
                pixaBayImageType,
                pixaBayPerPage,
                pixaSearch.category,
                pixaBaySafeSearch
            ).hits.toMutableList()
        }
        return ret

    }
}