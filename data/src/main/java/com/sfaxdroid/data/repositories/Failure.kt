package com.sfaxdroid.data.repositories

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object FeatureFailure : Failure()
}
