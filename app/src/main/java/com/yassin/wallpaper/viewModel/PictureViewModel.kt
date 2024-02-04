package com.yassin.wallpaper.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yassin.wallpaper.Model.PixaResponse
import com.yassin.wallpaper.network.ApiService
import kotlinx.coroutines.launch
import timber.log.Timber

const val pixaBaySearchTerm = "landscape"
const val pixaBayImageType = "photo"
//const val pixaBayOrientation = "vertical"
const val pixaBayPerPage = "200"
const val pixaBayCategory = "nature"
const val pixaBaySafeSearch = "true"
class PictureViewModel: ViewModel(){

    var pixaApiResponse:PixaResponse by mutableStateOf(PixaResponse(0,0, listOf()))
    var errorMessage:String by mutableStateOf("")
    fun getPictureList(){
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val response = apiService.getImages(
                    pixaBaySearchTerm,
                    pixaBayImageType,
                    //pixaBayOrientation,
                    pixaBayPerPage,
                    pixaBayCategory,
                    pixaBaySafeSearch
                )
                pixaApiResponse = response
                Timber.e("response: ${response.hits.size}")
            }catch (e:Exception){
                errorMessage = e.message.toString()
                Timber.e("error: $errorMessage")
            }
        }
    }
}