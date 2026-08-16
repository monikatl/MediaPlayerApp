package com.baszczyk.mediaplayerapp.repo
import android.util.Log
import com.baszczyk.mediaplayerapp.data.supabase.SupabaseProvider



import com.baszczyk.mediaplayerapp.models.Author
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.models.SongState
import com.baszczyk.mediaplayerapp.models.SongWithState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class SongRepositoryImpl() : SongRepository {

    private val supabase
        get() = SupabaseProvider.client

    override suspend fun getSongs(): Result<List<SongWithState>> {
        return runCatching {

            val userId = supabase.auth.currentUserOrNull()?.id
                ?: error("Użytkownik nie jest zalogowany")

            Log.d("SONGS_DEBUG", "userId = $userId")

            val songs = supabase
                .from("songs")
                .select()
                .decodeList<Song>()

            Log.d("SONGS_DEBUG", "songs = ${songs.size}")
            Log.d("SONGS_DEBUG", "songs data = $songs")

            val authors = supabase
                .from("authors")
                .select()
                .decodeList<Author>()

            Log.d("SONGS_DEBUG", "authors = ${authors.size}")
            Log.d("SONGS_DEBUG", "authors data = $authors")

            val states = supabase
                .from("song_states")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<SongState>()

            Log.d("SONGS_DEBUG", "states = ${states.size}")
            Log.d("SONGS_DEBUG", "states data = $states")

            val authorsById = authors.associateBy {
                it.id
            }

            val statesBySongId = states.associateBy {
                it.songId
            }

            Log.d(
                "SONGS_DEBUG",
                "authorsById = ${authorsById.keys}"
            )

            Log.d(
                "SONGS_DEBUG",
                "statesBySongId = ${statesBySongId.keys}"
            )

            songs.mapNotNull { song ->

                Log.d(
                    "SONGS_DEBUG",
                    "song id=${song.id}, authorId=${song.authorId}"
                )

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

    override suspend fun getSongUrl(storagePath: String?): String {
        TODO("Not yet implemented")
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