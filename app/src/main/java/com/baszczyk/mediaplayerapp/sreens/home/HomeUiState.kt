package com.baszczyk.mediaplayerapp.sreens.home

import com.baszczyk.mediaplayerapp.models.SongWithState

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val newSongs: List<SongWithState> = emptyList(),
    val recentlyListened: List<SongWithState> = emptyList()
)