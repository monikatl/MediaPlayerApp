package com.baszczyk.mediaplayerapp.service

sealed interface PlaybackCommand {
    data class Play(
        val songName: String,
        val songAuthor: String,
        val songUri: String
    ) : PlaybackCommand
    data object Pause : PlaybackCommand
    data object Resume : PlaybackCommand
    data object Stop : PlaybackCommand
}