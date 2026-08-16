package com.baszczyk.mediaplayerapp.repo

import com.baszczyk.mediaplayerapp.data.supabase.SupabaseProvider
import com.baszczyk.mediaplayerapp.models.User
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl : AuthRepository {

    private val supabase
        get() = SupabaseProvider.client

    override val authState: Flow<User?> =
        supabase.auth.sessionStatus.map { sessionStatus ->

            val currentUser = supabase.auth.currentUserOrNull()

            currentUser?.let {
                User(
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
        password: String
    ): Result<Unit> {

        return runCatching {

            supabase.auth.signUpWith(Email) {

                this.email = email

                this.password = password
            }
        }
    }

    override suspend fun logout(): Result<Unit> {

        return runCatching {
            supabase.auth.signOut()
        }
    }

    override fun currentUser(): User? {

        return supabase.auth.currentUserOrNull()?.let {

            User(
                id = it.id,
                email = it.email
            )
        }
    }
}