package com.sfaxdroid.detail

sealed class ActionTypeEnum {
    object OpenNativeChooser : ActionTypeEnum()
    object MovePerDir : ActionTypeEnum()
    object Crop : ActionTypeEnum()
    object SendLwp : ActionTypeEnum()
    object Delete : ActionTypeEnum()
    object Share : ActionTypeEnum()
}
