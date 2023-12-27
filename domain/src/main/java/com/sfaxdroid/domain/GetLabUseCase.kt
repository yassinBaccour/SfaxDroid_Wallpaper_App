package com.sfaxdroid.domain

import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.entity.WallpaperResponse
import com.sfaxdroid.data.repositories.WsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLabUseCase @Inject constructor(private val wsRepository: WsRepository) :
    ResultUseCase<GetLabUseCase.Param, Response<WallpaperResponse>>() {

    override suspend fun doWork(params: Param): Flow<Response<WallpaperResponse>> {
        return flow {
            emit(withContext(Dispatchers.IO) {
                wsRepository.getLab(params.file)
            })
        }
    }

    class Param(
        val file: String
    )
}
