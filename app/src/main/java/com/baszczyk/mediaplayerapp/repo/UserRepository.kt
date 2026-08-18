package com.baszczyk.mediaplayerapp.repo

import com.baszczyk.mediaplayerapp.models.User

interface UserRepository {
    suspend fun getUser(userId: String): User?

    suspend fun updateUsername(
        userId: String,
        username: String
    )
}