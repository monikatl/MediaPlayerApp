package com.baszczyk.mediaplayerapp.sreens.profile

data class ProfileUiState(
    val username: String = "",
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)