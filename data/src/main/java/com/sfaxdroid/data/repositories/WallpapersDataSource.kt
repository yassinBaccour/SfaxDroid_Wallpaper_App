package com.sfaxdroid.data.repositories

import com.sfaxdroid.data.DeviceNetworkHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WallpapersDataSource @Inject constructor(
    private val networkHandler: DeviceNetworkHandler,
    private val service: WsService
) :
    BaseNetwork(), WsRepository {

    override suspend fun getLiveWallpapers(file: String): Response {
        return when (networkHandler.isConnected()) {
            true -> request(service.getLiveWallpapers(file), { Response.SUCESS(it.copy()) }, null)
            false -> Response.FAILURE(Failure.NetworkConnection)
        }
    }

    override suspend fun getAllWallpaper(file: String): Response {
        return when (networkHandler.isConnected()) {
            true -> request(service.getAllWallpapers(file), { Response.SUCESS(it.copy()) }, null)
            false -> Response.FAILURE(Failure.NetworkConnection)
        }
    }

    override suspend fun getLab(file: String): Response {
        return when (networkHandler.isConnected()) {
            true -> request(service.getLab(file), { Response.SUCESS(it.copy()) }, null)
            false -> Response.FAILURE(Failure.NetworkConnection)
        }
    }

    override suspend fun getCategory(file: String): Response {
        return when (networkHandler.isConnected()) {
            true -> request(service.getCategory(file), { Response.SUCESS(it.copy()) }, null)
            false -> Response.FAILURE(Failure.NetworkConnection)
        }
    }

    override suspend fun getCategoryWallpaper(file: String): Response {
        return when (networkHandler.isConnected()) {
            true -> request(
                service.getCategoryWallpaper(file),
                { Response.SUCESS(it.copy()) },
                null
            )
            false -> Response.FAILURE(Failure.NetworkConnection)
        }
    }

    override suspend fun getTags(file: String): Response {
        return when (networkHandler.isConnected()) {
            true -> request(
                service.getTags(file),
                { Response.SUCESS(it) },
                null
            )
            false -> Response.FAILURE(Failure.NetworkConnection)
        }
    }
}
