package com.baszczyk.mediaplayerapp.sreens.mediaplayer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baszczyk.mediaplayerapp.player.ForegroundManager
import com.baszczyk.mediaplayerapp.repo.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val foregroundManager: ForegroundManager,
    private val repository: SongRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            MediaPlayerUiState()
        )

    val uiState: StateFlow<MediaPlayerUiState> =
        _uiState.asStateFlow()

    fun playSongById(
        songId: Long
    ) {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

            repository.getSongById(songId)
                .onSuccess { songWithState ->

                    try {

                        _uiState.value =
                            _uiState.value.copy(
                                currentSong =
                                    songWithState
                            )

                        val storagePath =
                            songWithState
                                .song
                                .storagePath

                        Log.d(
                            "PLAYER",
                            "storagePath = $storagePath"
                        )

                        val audioUrl =
                            repository.getSongUrl(
                                storagePath
                            )

                        Log.d(
                            "PLAYER",
                            "audioUrl = $audioUrl"
                        )

                        foregroundManager.play(
                            songWithState =
                                songWithState,

                            audioUrl =
                                audioUrl
                        )

                        repository.updateListened(
                            songId =
                                songWithState.song.id,

                            isListened =
                                true
                        )

                        _uiState.value =
                            _uiState.value.copy(
                                isLoading = false,
                                isPlaying = true
                            )

                    } catch (e: Exception) {

                        Log.e(
                            "PLAYER",
                            "Błąd odtwarzania",
                            e
                        )

                        _uiState.value =
                            _uiState.value.copy(
                                isLoading = false,
                                isPlaying = false,
                                error =
                                    e.message
                                        ?: "Nie udało się odtworzyć piosenki"
                            )
                    }
                }
                .onFailure { error ->

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isPlaying = false,
                            error =
                                error.message
                                    ?: "Nie udało się pobrać piosenki"
                        )
                }
        }
    }

    fun pause() {
        foregroundManager.pause()
        _uiState.value =
            _uiState.value.copy(
                isPlaying = false
            )
    }

    fun resume() {
        foregroundManager.resume()
        _uiState.value =
            _uiState.value.copy(
                isPlaying = true
            )
    }

    fun stop() {
        foregroundManager.stop()
        _uiState.value =
            _uiState.value.copy(
                isPlaying = false,
                position = 0L
            )
    }
}