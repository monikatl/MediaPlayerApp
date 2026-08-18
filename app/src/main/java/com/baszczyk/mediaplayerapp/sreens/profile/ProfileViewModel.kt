package com.baszczyk.mediaplayerapp.sreens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baszczyk.mediaplayerapp.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadUser(userId: String) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val user = userRepository.getUser(userId)

                _uiState.update {
                    it.copy(
                        username = user?.name.orEmpty(),
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Nie udało się pobrać profilu"
                    )
                }
            }
        }
    }

    fun onUsernameChange(username: String) {
        _uiState.update {
            it.copy(
                username = username,
                error = null
            )
        }
    }

    fun startEditing() {
        _uiState.update {
            it.copy(
                isEditing = true,
                error = null
            )
        }
    }

    fun cancelEditing() {
        _uiState.update {
            it.copy(
                isEditing = false,
                error = null
            )
        }
    }

    fun saveUsername(userId: String) {
        val username = _uiState.value.username.trim()

        if (username.isBlank()) {
            _uiState.update {
                it.copy(
                    error = "Nazwa użytkownika nie może być pusta"
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {

                userRepository.updateUsername(
                    userId = userId,
                    username = username
                )

                _uiState.update {
                    it.copy(
                        username = username,
                        isEditing = false,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Nie udało się zapisać nazwy"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }
}