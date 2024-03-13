package com.sfaxdroid.domain

import com.sfaxdroid.data.entity.Response
import com.sfaxdroid.data.entity.TagResponse
import com.sfaxdroid.data.repositories.WsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTagUseCase @Inject constructor(private val wsRepository: WsRepository) :
    ResultUseCase<GetTagUseCase.Param, Response<TagResponse>>() {

    override suspend fun doWork(params: Param): Flow<Response<TagResponse>> {
        return flow {
            emit(withContext(Dispatchers.IO) {
                wsRepository.getTags(params.screenName)
            })
        }
    }

    class Param(
        val screenName: String
    )
}
