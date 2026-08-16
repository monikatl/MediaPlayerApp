package com.baszczyk.mediaplayerapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val id: Long,
    val name: String,
    val description: String? = null,

    @SerialName("image_url")
    val imageUrl: String? = null
)