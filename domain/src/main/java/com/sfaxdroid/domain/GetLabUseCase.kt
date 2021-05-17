package com.sfaxdroid.domain

import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.repositories.WsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLabUseCase @Inject constructor(private val wsRepository: WsRepository) :
    ResultUseCase<GetLabUseCase.Param, Response<WallpaperResponse>>() {

    override suspend fun doWork(params: Param): Response<WallpaperResponse> {
        return withContext(Dispatchers.IO) {
            wsRepository.getLab(params.file)
        }
    }

    class Param(
        val file: String
    )
}
