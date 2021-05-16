package com.sfaxdroid.data.entity

import com.google.gson.annotations.SerializedName

data class TagResponse(@SerializedName("Tags") val tagList: List<Tag>)
