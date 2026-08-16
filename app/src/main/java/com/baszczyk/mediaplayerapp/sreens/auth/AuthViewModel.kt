package com.baszczyk.mediaplayerapp.sreens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baszczyk.mediaplayerapp.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState()
    )

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    init {
        observeAuth()
    }

    private fun observeAuth() {

        viewModelScope.launch {

            repository.authState.collect { user ->

                _uiState.value =
                    _uiState.value.copy(
                        user = user,
                        isLoggedIn = user != null
                    )
            }
        }
    }

    fun updateEmail(
        email: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                email = email,
                error = null
            )
    }

    fun updatePassword(
        password: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                password = password,
                error = null
            )
    }

    fun login() {

        val email =
            _uiState.value.email.trim()

        val password =
            _uiState.value.password

        if (email.isBlank()) {

            _uiState.value =
                _uiState.value.copy(
                    error = "Podaj adres email"
                )

            return
        }

        if (password.isBlank()) {

            _uiState.value =
                _uiState.value.copy(
                    error = "Podaj hasło"
                )

            return
        }

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

            repository
                .login(
                    email = email,
                    password = password
                )
                .onSuccess {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = null
                        )
                }
                .onFailure { error ->

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = error.message
                                ?: "Nie udało się zalogować"
                        )
                }
        }
    }

    fun register() {

        val email =
            _uiState.value.email.trim()

        val password =
            _uiState.value.password

        if (email.isBlank()) {

            _uiState.value =
                _uiState.value.copy(
                    error = "Podaj adres email"
                )

            return
        }

        if (password.length < 6) {

            _uiState.value =
                _uiState.value.copy(
                    error = "Hasło musi mieć minimum 6 znaków"
                )

            return
        }

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    error = null,
                    registerSuccess = false
                )

            repository
                .register(
                    email = email,
                    password = password
                )
                .onSuccess {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            registerSuccess = true,
                            error = null
                        )
                }
                .onFailure { error ->

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = error.message
                                ?: "Nie udało się utworzyć konta"
                        )
                }
        }
    }

    fun logout() {

        viewModelScope.launch {

            repository
                .logout()
                .onFailure { error ->

                    _uiState.value =
                        _uiState.value.copy(
                            error = error.message
                                ?: "Nie udało się wylogować"
                        )
                }
        }
    }

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }

    fun clearRegisterSuccess() {

        _uiState.value =
            _uiState.value.copy(
                registerSuccess = false
            )
    }
}