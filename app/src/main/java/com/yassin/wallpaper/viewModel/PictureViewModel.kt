package com.yassin.wallpaper.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yassin.wallpaper.Model.PixaPicture
import com.yassin.wallpaper.network.ApiService
import kotlinx.coroutines.launch

const val pixaBayApiKey = "19985524-f627984e6e929e47e060fd2ff"
const val pixaBaySearchTerm = "nature"
const val pixaBayImageType = "photo"
const val pixaBayOrientation = "vertical"
class PictureViewModel: ViewModel(){

    var pictureListResponse:List<PixaPicture> by mutableStateOf(listOf())
    var errorMessage:String by mutableStateOf("")
    fun getPictureList(){
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val response = apiService.getImages(
                    pixaBayApiKey,
                    pixaBaySearchTerm,
                    pixaBayImageType,
                    pixaBayOrientation
                )
                pictureListResponse = response
            }catch (e:Exception){
                errorMessage = e.message.toString()
            }
        }
    }
}