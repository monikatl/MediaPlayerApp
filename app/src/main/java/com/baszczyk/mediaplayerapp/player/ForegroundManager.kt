package com.baszczyk.mediaplayerapp.player

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.service.PlaybackService

class ForegroundManager(
    private val context: Context
) {

    fun play(
        song: Song?,
        audioUrl: String
    ) {

        val intent = Intent(
            context,
            PlaybackService::class.java
        ).apply {

            action = PlaybackService.ACTION_PLAY

            putExtra(
                PlaybackService.EXTRA_SONG_ID,
                song?.id
            )

            putExtra(
                PlaybackService.EXTRA_SONG_NAME,
                song?.name
            )

            putExtra(
                PlaybackService.EXTRA_SONG_AUTHOR,
                song?.author
            )

            putExtra(
                PlaybackService.EXTRA_SONG_URI,
                audioUrl
            )
        }

        ContextCompat.startForegroundService(
            context,
            intent
        )
    }

    fun pause() {
        sendAction(
            PlaybackService.ACTION_PAUSE
        )
    }

    fun resume() {
        sendAction(
            PlaybackService.ACTION_RESUME
        )
    }

    fun stop() {
        sendAction(
            PlaybackService.ACTION_STOP
        )
    }

    private fun sendAction(
        action: String
    ) {

        val intent = Intent(
            context,
            PlaybackService::class.java
        ).apply {
            this.action = action
        }

        context.startService(intent)
    }
}