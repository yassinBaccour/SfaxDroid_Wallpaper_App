package com.sfaxdroid.bases

import java.net.URLDecoder
import java.net.URLEncoder

fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8").orEmpty()
fun String.decodeUrl() = URLDecoder.decode(this, "UTF-8").orEmpty()