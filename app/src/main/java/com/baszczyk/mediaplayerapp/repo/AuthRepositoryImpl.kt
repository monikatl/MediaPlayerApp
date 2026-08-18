package com.baszczyk.mediaplayerapp.repo

import com.baszczyk.mediaplayerapp.data.supabase.SupabaseProvider
import com.baszczyk.mediaplayerapp.models.AuthUser
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepositoryImpl : AuthRepository {

    private val supabase
        get() = SupabaseProvider.client

    override val authState: Flow<AuthUser?> =
        supabase.auth.sessionStatus.map {

            supabase.auth.currentUserOrNull()?.let {
                AuthUser(
                    id = it.id,
                    email = it.email
                )
            }
        }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {

        return runCatching {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<Unit> {

        return runCatching {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password

                data = buildJsonObject {
                    put("name", name)
                }
            }
        }
    }

    override suspend fun logout(): Result<Unit> {

        return runCatching {
            supabase.auth.signOut()
        }
    }

    override fun currentUser(): AuthUser? {

        return supabase.auth.currentUserOrNull()?.let {
            AuthUser(
                id = it.id,
                email = it.email
            )
        }
    }
}