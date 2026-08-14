package com.baszczyk.mediaplayerapp.sreens.mediaplayer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.player.ForegroundManager
import com.baszczyk.mediaplayerapp.repo.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class MediaPlayerUiState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MediaPlayerViewModel(
    private val foregroundManager: ForegroundManager,
    private val repository: SongRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaPlayerUiState())
    val uiState: StateFlow<MediaPlayerUiState> = _uiState.asStateFlow()

    fun playSongById(songId: Long) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val song = repository.getSongById(songId)

                _uiState.value = _uiState.value.copy(
                    currentSong = song
                )

                val audioUrl = repository.getSongUrl(
                    song?.storagePath ?: ""
                )

                Log.d("PLAYER", "storagePath = ${song?.storagePath}")
                Log.d("PLAYER", "audioUrl = $audioUrl")

                foregroundManager.play(
                    song = song,
                    audioUrl = audioUrl
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isPlaying = true
                )

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isPlaying = false,
                    error = e.message
                        ?: "Nie udało się odtworzyć piosenki"
                )
            }
        }
    }


    fun pause() {
        foregroundManager.pause()
    }

    fun resume() {
        foregroundManager.resume()
    }

    fun stop() {
        foregroundManager.stop()
    }
}