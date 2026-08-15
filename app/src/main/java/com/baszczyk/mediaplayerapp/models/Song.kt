package com.baszczyk.mediaplayerapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Song(
    val id: Long,
    val name: String,
    val author: String,
    val duration: Long,
    val uri: String,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("storage_path")
    val storagePath: String = "",

    val isFavorite: Boolean = false,
    val isListened: Boolean = false
)