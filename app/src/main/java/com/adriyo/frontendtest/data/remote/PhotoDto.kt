package com.adriyo.frontendtest.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    val id: Int,
    val albumId: Int,
    val title: String?,
    val url: String?,
    val thumbnailUrl: String?
)
