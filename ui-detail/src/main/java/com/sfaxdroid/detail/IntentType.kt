package com.sfaxdroid.detail

sealed class IntentType {
    object FACEBOOK : IntentType()
    object INSTAGRAM : IntentType()
    object SNAP : IntentType()
}