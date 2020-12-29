package com.sfaxdroid.data.mappers

interface SfaxDroidMapper<F, T> {
    fun map(from: F?): T
}