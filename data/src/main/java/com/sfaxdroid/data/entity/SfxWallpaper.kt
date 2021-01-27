package com.sfaxdroid.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class SfxWallpaper(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0) :
    SfaxDroidEntity {
}