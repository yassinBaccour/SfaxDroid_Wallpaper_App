package com.sfaxdroid.data.entity

import com.google.gson.annotations.SerializedName

data class TagResponse(@SerializedName("Tags") var tagList: List<Tag>)