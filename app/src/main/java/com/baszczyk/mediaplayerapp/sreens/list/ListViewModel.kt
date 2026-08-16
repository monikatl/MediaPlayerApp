package com.baszczyk.mediaplayerapp.sreens.list

import ListUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baszczyk.mediaplayerapp.models.SongWithState
import com.baszczyk.mediaplayerapp.repo.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: SongRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())

    val uiState: StateFlow<ListUiState> =
        _uiState.asStateFlow()

    init {
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository
                .getSongs()
                .onSuccess { songs ->

                    _uiState.value = ListUiState(
                        songs = songs,
                        isLoading = false,
                        error = null
                    )
                    println("SIZE--- ${songs.size}")
                }
                .onFailure { error ->

                    Log.e(
                        "SONG_REPOSITORY",
                        "Błąd podczas pobierania piosenek",
                        error
                    )

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                            ?: "Nie udało się pobrać piosenek"
                    )
                }
        }
    }
}