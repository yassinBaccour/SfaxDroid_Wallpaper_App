package com.sfaxdroid.base.extension

fun String.getFileName() = this.substring(this.lastIndexOf('/') + 1, this.length)
