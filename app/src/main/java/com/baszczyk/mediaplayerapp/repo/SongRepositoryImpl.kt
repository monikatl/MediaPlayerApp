package com.baszczyk.mediaplayerapp.repo
import android.util.Log
import com.baszczyk.mediaplayerapp.data.supabase.SupabaseProvider

import com.baszczyk.mediaplayerapp.models.Author
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.models.SongState
import com.baszczyk.mediaplayerapp.models.SongWithState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class SongRepositoryImpl() : SongRepository {

    private val supabase
        get() = SupabaseProvider.client

    override suspend fun getSongs(): Result<List<SongWithState>> {
        return runCatching {

            val userId = supabase.auth.currentUserOrNull()?.id
                ?: error("Użytkownik nie jest zalogowany")


            val songs = supabase
                .from("songs")
                .select()
                .decodeList<Song>()

            val authors = supabase
                .from("authors")
                .select()
                .decodeList<Author>()

            val states = supabase
                .from("song_states")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<SongState>()

            val authorsById = authors.associateBy {
                it.id
            }

            val statesBySongId = states.associateBy {
                it.songId
            }

            songs.mapNotNull { song ->

                val author = authorsById[song.authorId]

                if (author == null) {
                    Log.e(
                        "SONGS_DEBUG",
                        "BRAK AUTORA dla songId=${song.id}, authorId=${song.authorId}"
                    )

                    return@mapNotNull null
                }

                val state = statesBySongId[song.id]
                    ?: SongState(
                        songId = song.id,
                        userId = userId,
                        isFavorite = false,
                        isListened = false,
                        isNew = true
                    )

                SongWithState(
                    song = song,
                    author = author,
                    state = state
                )
            }.also {
                Log.d(
                    "SONGS_DEBUG",
                    "FINAL SONGS = ${it.size}"
                )
            }
        }
    }

    override suspend fun updateFavorite(
        songId: Long,
        isFavorite: Boolean
    ): Result<Unit> {
        return updateState(
            songId = songId,
            isFavorite = isFavorite
        )
    }

    override suspend fun updateListened(
        songId: Long,
        isListened: Boolean
    ): Result<Unit> {
        return updateState(
            songId = songId,
            isListened = isListened
        )
    }

    override suspend fun updateNew(
        songId: Long,
        isNew: Boolean
    ): Result<Unit> {
        return updateState(
            songId = songId,
            isNew = isNew
        )
    }

    override suspend fun getSongById(
        songId: Long
    ): Result<SongWithState> {

        return runCatching {

            val userId = supabase.auth.currentUserOrNull()?.id
                ?: error("Użytkownik nie jest zalogowany")

            val song = supabase
                .from("songs")
                .select {
                    filter {
                        eq("id", songId)
                    }
                }
                .decodeSingle<Song>()

            val author = supabase
                .from("authors")
                .select {
                    filter {
                        eq("id", song.authorId)
                    }
                }
                .decodeSingle<Author>()

            val state = supabase
                .from("song_states")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("song_id", songId)
                    }
                }
                .decodeList<SongState>()
                .firstOrNull()
                ?: SongState(
                    songId = songId,
                    userId = userId,
                    isFavorite = false,
                    isListened = false,
                    isNew = true
                )

            SongWithState(
                song = song,
                author = author,
                state = state
            )
        }
    }

    override suspend fun getSongUrl(
        storagePath: String?
    ): String {

        return runCatching {

            val path = storagePath
                ?: error("Ścieżka utworu nie może być pusta")

            Log.d("STORAGE", "bucket = songs")
            Log.d("STORAGE", "path = '$path'")

            supabase
                .storage
                .from("songs")
                .publicUrl(path)

        }.getOrThrow()
    }

    private suspend fun updateState(
        songId: Long,
        isFavorite: Boolean? = null,
        isListened: Boolean? = null,
        isNew: Boolean? = null
    ): Result<Unit> {
        return runCatching {

            val userId = supabase.auth.currentUserOrNull()?.id
                ?: error("Użytkownik nie jest zalogowany")

            val currentState = supabase
                .from("song_states")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("song_id", songId)
                    }
                }
                .decodeSingleOrNull<SongState>()

            val newState = SongState(
                songId = songId,
                userId = userId,
                isFavorite = isFavorite
                    ?: currentState?.isFavorite
                    ?: false,
                isListened = isListened
                    ?: currentState?.isListened
                    ?: false,
                isNew = isNew
                    ?: currentState?.isNew
                    ?: true
            )

            supabase
                .from("song_states")
                .upsert(newState)
        }
    }
}