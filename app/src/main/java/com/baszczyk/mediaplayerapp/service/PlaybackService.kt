package com.baszczyk.mediaplayerapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.baszczyk.mediaplayerapp.MainActivity
import com.baszczyk.mediaplayerapp.R

@OptIn(UnstableApi::class)
class PlaybackService : MediaSessionService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        player = ExoPlayer.Builder(this)
            .build()

        val openAppIntent = Intent(
            this,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationProvider = DefaultMediaNotificationProvider.Builder(this)
            .setChannelName(R.string.media_notification_channel_name)
            .build()

        setMediaNotificationProvider(notificationProvider)

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(pendingIntent)
            .build()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        stopSong()
                    }
                }
            }
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.media_notification_channel_id),
                getString(R.string.media_notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.media_notification_channel_description)
                setShowBadge(false)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (val command = intent?.toPlaybackCommand()) {
            is PlaybackCommand.Play -> playSong(command)
            PlaybackCommand.Pause -> pauseSong()
            PlaybackCommand.Resume -> resumeSong()
            PlaybackCommand.Stop -> stopSong()
            null -> Unit
        }
        return START_NOT_STICKY
    }

    private fun playSong(command: PlaybackCommand.Play) {
        playSong(
            songUri = command.songUri,
            songName = command.songName,
            songAuthor = command.songAuthor
        )
        startForeground(
            NOTIFICATION_ID,
            createNotification()
        )
    }

    private fun playSong(
        songUri: String,
        songName: String,
        songAuthor: String
    ) {
        val mediaItem = MediaItem.Builder()
            .setUri(songUri)
            .setMediaId(songName)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(songName)
                    .setArtist(songAuthor)
                    .build()
            )
            .build()

        setPrepareAndPlay(mediaItem)
        isPlaying = true
    }

    private fun pauseSong() {
        player.pause()
        isPlaying = false
        updateNotification()
    }

    private fun resumeSong() {
        player.play()
        isPlaying = true
        updateNotification()
    }

    private fun setPrepareAndPlay(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun stopSong() {
        player.stop()
        player.clearMediaItems()
        isPlaying = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, getString(R.string.media_notification_channel_id))
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_notification)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification() {
        val notification = createNotification()
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onDestroy() {
        mediaSession.release()
        player.release()
        super.onDestroy()
    }

    companion object {
        const val ACTION_PLAY = "com.baszczyk.mediaplayerapp.PLAY"
        const val ACTION_PAUSE = "com.baszczyk.mediaplayerapp.PAUSE"
        const val ACTION_RESUME = "com.baszczyk.mediaplayerapp.RESUME"
        const val ACTION_STOP = "com.baszczyk.mediaplayerapp.STOP"
        const val EXTRA_SONG_ID = "song_id"
        const val EXTRA_SONG_NAME = "song_name"
        const val EXTRA_SONG_AUTHOR = "song_author"
        const val EXTRA_SONG_URI = "song_uri"
        private const val NOTIFICATION_ID = 1001
    }
}