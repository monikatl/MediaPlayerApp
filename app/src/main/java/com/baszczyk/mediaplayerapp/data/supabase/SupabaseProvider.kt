package com.baszczyk.mediaplayerapp.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseProvider {

    lateinit var client: SupabaseClient
        private set

    fun initialize(
        url: String,
        key: String
    ) {
        client = createSupabaseClient(
            supabaseUrl = url,
            supabaseKey = key
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
        }
    }
}