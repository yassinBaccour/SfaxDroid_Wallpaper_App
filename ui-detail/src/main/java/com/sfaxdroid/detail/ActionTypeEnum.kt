package com.sfaxdroid.detail

sealed class ActionTypeEnum {
    object ShareFacebook : ActionTypeEnum()
    object ShareInstagram : ActionTypeEnum()
    object OpenNativeChooser : ActionTypeEnum()
    object MovePerDir : ActionTypeEnum()
    object Crop : ActionTypeEnum()
    object ShareSnap : ActionTypeEnum()
    object SendLwp : ActionTypeEnum()
    object Delete : ActionTypeEnum()
    object JustWallpaper : ActionTypeEnum()
}