package com.baszczyk.mediaplayerapp.service

import android.content.Intent

fun Intent.toPlaybackCommand(): PlaybackCommand? {
    return when (action) {
        PlaybackService.ACTION_PLAY -> toPlayCommand()
        PlaybackService.ACTION_PAUSE ->  PlaybackCommand.Pause
        PlaybackService.ACTION_RESUME ->  PlaybackCommand.Resume
        PlaybackService.ACTION_STOP ->  PlaybackCommand.Stop
        else -> null
    }
}

private fun Intent.toPlayCommand(): PlaybackCommand.Play? {
    val songUri = getStringExtra(PlaybackService.EXTRA_SONG_URI) ?: return null
    val songName = getStringExtra(PlaybackService.EXTRA_SONG_NAME) ?: "Nieznany utwór"
    val songAuthor =  getStringExtra(PlaybackService.EXTRA_SONG_AUTHOR) ?: "Nieznany autor"

    return PlaybackCommand.Play(
        songName = songName,
        songAuthor = songAuthor,
        songUri = songUri
    )
}