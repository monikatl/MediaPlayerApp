package com.baszczyk.mediaplayerapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.baszczyk.mediaplayerapp.MainActivity
import com.baszczyk.mediaplayerapp.R

class PlaybackService : Service() {

    private lateinit var player: ExoPlayer

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        player = ExoPlayer.Builder(this)
            .build()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when (intent?.action) {

            ACTION_PLAY -> {

                val songName =
                    intent.getStringExtra(EXTRA_SONG_NAME)
                        ?: "Nieznany utwór"

                val songAuthor =
                    intent.getStringExtra(EXTRA_SONG_AUTHOR)
                        ?: "Nieznany autor"

                val songUri =
                    intent.getStringExtra(EXTRA_SONG_URI)

                if (songUri != null) {

                    playSong(
                        songUri = songUri,
                        songName = songName,
                        songAuthor = songAuthor
                    )
                }
            }

            ACTION_PAUSE -> {
                player.pause()
                updateNotification()
            }

            ACTION_RESUME -> {
                player.play()
                updateNotification()
            }

            ACTION_STOP -> {

                player.stop()

                stopForeground(
                    STOP_FOREGROUND_REMOVE
                )

                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    private fun playSong(
        songUri: String,
        songName: String,
        songAuthor: String
    ) {

        val mediaItem = MediaItem.Builder()
            .setUri(songUri)
            .setMediaId(
                songName
            )
            .build()

        player.setMediaItem(mediaItem)

        player.prepare()

        player.play()

        startForeground(
            NOTIFICATION_ID,
            createNotification(
                songName = songName,
                songAuthor = songAuthor
            )
        )
    }

    private fun createNotification(
        songName: String,
        songAuthor: String
    ): Notification {

        val openAppIntent = Intent(
            this,
            MainActivity::class.java
        )

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                openAppIntent,
                PendingIntent.FLAG_IMMUTABLE or
                        PendingIntent.FLAG_UPDATE_CURRENT
            )

        return NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(
                R.drawable.ic_launcher_foreground
            )
            .setContentTitle(songName)
            .setContentText(songAuthor)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(
                NotificationCompat.PRIORITY_LOW
            )
            .build()
    }

    private fun updateNotification() {

        val notificationManager =
            getSystemService(
                NotificationManager::class.java
            )

        val notification =
            createNotification(
                songName = "Odtwarzacz",
                songAuthor =
                    if (player.isPlaying)
                        "Odtwarzanie"
                    else
                        "Pauza"
            )

        notificationManager.notify(
            NOTIFICATION_ID,
            notification
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Odtwarzanie muzyki",
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager =
            getSystemService(
                NotificationManager::class.java
            )

        notificationManager.createNotificationChannel(
            channel
        )
    }

    override fun onDestroy() {

        player.release()

        super.onDestroy()
    }

    override fun onBind(
        intent: Intent?
    ): IBinder? {
        return null
    }

    companion object {

        const val ACTION_PLAY =
            "com.baszczyk.mediaplayerapp.PLAY"

        const val ACTION_PAUSE =
            "com.baszczyk.mediaplayerapp.PAUSE"

        const val ACTION_RESUME =
            "com.baszczyk.mediaplayerapp.RESUME"

        const val ACTION_STOP =
            "com.baszczyk.mediaplayerapp.STOP"

        const val EXTRA_SONG_ID =
            "song_id"

        const val EXTRA_SONG_NAME =
            "song_name"

        const val EXTRA_SONG_AUTHOR =
            "song_author"

        const val EXTRA_SONG_URI =
            "song_uri"

        private const val CHANNEL_ID =
            "media_playback"

        private const val NOTIFICATION_ID = 1001
    }
}