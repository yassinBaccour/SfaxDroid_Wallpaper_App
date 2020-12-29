package com.sfaxdroid.data.repositories

sealed class Failure {
    class NetworkConnection : Failure()
    class ServerError : Failure()
    class FeatureFailure : Failure()
}
