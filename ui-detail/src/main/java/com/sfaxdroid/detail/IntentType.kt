package com.sfaxdroid.detail

sealed class IntentType {
    object facebook : IntentType()
    object instagram : IntentType()
    object snap : IntentType()
}