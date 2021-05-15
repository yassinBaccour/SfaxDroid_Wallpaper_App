package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetLiveWallpapersUseCase @Inject constructor(private val wsRepository: WsRepository) :
    ResultUseCase<GetLiveWallpapersUseCase.Param, Response>() {

    override suspend fun doWork(params: Param): Response {
        return wsRepository.getLiveWallpapers(params.file)
    }

    class Param(
        val file: String
    )

}
