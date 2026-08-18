package com.baszczyk.mediaplayerapp.repo

import com.baszczyk.mediaplayerapp.models.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val authState: Flow<AuthUser?>

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit>

    suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<Unit>

    suspend fun logout(): Result<Unit>

    fun currentUser(): AuthUser?
}