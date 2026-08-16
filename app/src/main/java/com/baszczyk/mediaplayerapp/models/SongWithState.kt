package com.baszczyk.mediaplayerapp.models

data class SongWithState(
    val song: Song,
    val author: Author,
    val state: SongState
)