package com.baszczyk.mediaplayerapp.sreens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baszczyk.mediaplayerapp.repo.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {

            _uiState.value = HomeUiState(
                isLoading = true
            )

            songRepository
                .getSongs()
                .onSuccess { songs ->

                    val newSongs = songs.filter {
                        it.state.isNew
                    }

                    val recentlyListened = songs.filter {
                        it.state.isListened
                    }

                    _uiState.value = HomeUiState(
                        isLoading = false,
                        newSongs = newSongs,
                        recentlyListened = recentlyListened
                    )
                }
                .onFailure { error ->

                    _uiState.value = HomeUiState(
                        isLoading = false,
                        error = error.message
                            ?: "Nie udało się pobrać danych."
                    )
                }
        }
    }
}