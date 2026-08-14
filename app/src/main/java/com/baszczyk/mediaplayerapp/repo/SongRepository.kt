package com.baszczyk.mediaplayerapp.repo

import com.baszczyk.mediaplayerapp.data.supabase.SupabaseProvider
import com.baszczyk.mediaplayerapp.models.Song
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class SongRepository {

    private val supabase
        get() = SupabaseProvider.client


    suspend fun getSongs(): List<Song> {

        return supabase
            .from("songs")
            .select()
            .decodeList<Song>()
    }


    suspend fun getSongById(
        id: Long
    ): Song? {

        return supabase
            .from("songs")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<Song>()
            .firstOrNull()
    }


    fun getSongUrl(
        storagePath: String
    ): String {

        return supabase
            .storage
            .from("songs")
            .publicUrl(storagePath)
    }


    fun getImageUrl(
        imagePath: String
    ): String {

        return supabase
            .storage
            .from("songs")
            .publicUrl(imagePath)
    }
}