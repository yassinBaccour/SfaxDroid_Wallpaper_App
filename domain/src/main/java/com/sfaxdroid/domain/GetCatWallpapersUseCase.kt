package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import javax.inject.Inject

class GetCatWallpapersUseCase @Inject constructor(private val wsRepository: WsRepository) :
    ResultUseCase<GetCatWallpapersUseCase.Param, Response>() {

    override suspend fun doWork(params: Param): Response {
        return wsRepository.getCategoryWallpaper(params.file)
    }

    class Param(
        val file: String
    )
}
