package com.sfaxdroid.domain.config

import javax.inject.Inject

class AppConfig @Inject constructor(
    val partnerBaseUrl: String,
    val partnerApiKey: String,
    val sfaxDroidBaseUrl: String,
    val jsonVersion: String
)