package com.baszczyk.mediaplayerapp.repo

import com.baszczyk.mediaplayerapp.models.SongWithState

interface SongRepository {

    suspend fun getSongs(): Result<List<SongWithState>>

    suspend fun updateFavorite(
        songId: Long,
        isFavorite: Boolean
    ): Result<Unit>

    suspend fun updateListened(
        songId: Long,
        isListened: Boolean
    ): Result<Unit>

    suspend fun updateNew(
        songId: Long,
        isNew: Boolean
    ): Result<Unit>

    suspend fun getSongById(
        songId: Long
    ): Result<SongWithState>

    suspend fun getSongUrl(
        storagePath: String?
    ): String
}