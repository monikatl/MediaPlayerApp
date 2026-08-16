package com.baszczyk.mediaplayerapp.sreens.auth

import com.baszczyk.mediaplayerapp.models.User

data class AuthUiState(
    val email: String = "",
    val password: String = "",

    val isLoading: Boolean = false,

    val user: User? = null,

    val isLoggedIn: Boolean = false,

    val error: String? = null,

    val registerSuccess: Boolean = false
)