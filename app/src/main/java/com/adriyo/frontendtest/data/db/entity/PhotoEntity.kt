package com.adriyo.frontendtest.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("photos")
data class PhotoEntity(
    @PrimaryKey
    val id: Int,
    val albumId: Int,
    val title: String?,
    val url: String?,
    val thumbnailUrl: String?,
)
