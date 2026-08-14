package com.baszczyk.mediaplayerapp.sreens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.repo.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ListUiState(
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ListViewModel(
    private val repository: SongRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val songs = repository.getSongs()

                _uiState.value = ListUiState(
                    songs = songs,
                    isLoading = false,
                    error = null
                )

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Nie udało się pobrać piosenek"
                )
            }
        }
    }
}