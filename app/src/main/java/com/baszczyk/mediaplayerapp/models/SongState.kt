package com.baszczyk.mediaplayerapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongState(
    @SerialName("song_id")
    val songId: Long,

    @SerialName("user_id")
    val userId: String,

    @SerialName("is_favorite")
    val isFavorite: Boolean = false,

    @SerialName("is_listened")
    val isListened: Boolean = false,

    @SerialName("is_new")
    val isNew: Boolean = true
)