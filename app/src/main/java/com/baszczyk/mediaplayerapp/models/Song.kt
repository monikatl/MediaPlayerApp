package com.baszczyk.mediaplayerapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Song(
    val id: Long,
    val name: String,
    val duration: Long,
    val uri: String,

    @SerialName("author_id")
    val authorId: Long,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("storage_path")
    val storagePath: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)