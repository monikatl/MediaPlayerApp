package com.baszczyk.mediaplayerapp.repo

import com.baszczyk.mediaplayerapp.data.supabase.SupabaseProvider
import com.baszczyk.mediaplayerapp.models.User
import io.github.jan.supabase.postgrest.from

class UserRepositoryImpl : UserRepository {

    private val supabase
        get() = SupabaseProvider.client

    override suspend fun getUser(userId: String): User? {
        return supabase
            .from("users")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingleOrNull<User>()
    }

    override suspend fun updateUsername(
        userId: String,
        username: String
    ) {
        supabase
            .from("users")
            .update(
                {
                    set("name", username)
                }
            ) {
                filter {
                    eq("id", userId)
                }
            }
    }
}