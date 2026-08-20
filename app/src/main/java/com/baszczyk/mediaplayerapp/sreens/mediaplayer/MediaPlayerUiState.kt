package com.baszczyk.mediaplayerapp.sreens.mediaplayer

import com.baszczyk.mediaplayerapp.models.SongWithState

data class MediaPlayerUiState(
    val currentSong: SongWithState? = null,
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val duration: Long = 0L,
    val error: String? = null
)