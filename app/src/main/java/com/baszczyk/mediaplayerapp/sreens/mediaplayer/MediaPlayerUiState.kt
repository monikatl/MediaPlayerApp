package com.baszczyk.mediaplayerapp.sreens.mediaplayer

import com.baszczyk.mediaplayerapp.models.SongWithState

data class MediaPlayerUiState(
    val currentSong: SongWithState? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)