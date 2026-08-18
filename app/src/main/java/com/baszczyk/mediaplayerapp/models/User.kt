package com.baszczyk.mediaplayerapp.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String? = null
)