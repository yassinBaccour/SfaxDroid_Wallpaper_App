package com.sfaxdroid.domain

import com.sfaxdroid.data.repositories.Response
import com.sfaxdroid.data.repositories.WsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCatWallpapersUseCase @Inject constructor(private val wsRepository: WsRepository) :
    ResultUseCase<GetCatWallpapersUseCase.Param, Response>() {

    override suspend fun doWork(params: Param): Response {
        return withContext(Dispatchers.IO) {
            wsRepository.getCategoryWallpaper(params.file)
        }
    }

    class Param(
        val file: String
    )
}
