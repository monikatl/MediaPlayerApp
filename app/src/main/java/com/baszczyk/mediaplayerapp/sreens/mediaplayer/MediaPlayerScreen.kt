package com.baszczyk.mediaplayerapp.sreens.mediaplayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaPlayerScreen(
    songId: Long?,
    viewModel: MediaPlayerViewModel =
        koinViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(songId) {

        if (songId != null) {
            viewModel.playSongById(songId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text =
                state.currentSong?.name
                    ?: "Brak utworu"
        )

        Text(
            text =
                state.currentSong?.author
                    ?: ""
        )

        Row(
            horizontalArrangement =
                Arrangement.Center,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {

                    if (state.isPlaying) {
                        viewModel.pause()
                    } else {
                        viewModel.resume()
                    }
                }
            ) {

                Icon(
                    imageVector =
                        if (state.isPlaying)
                            Icons.Default.Pause
                        else
                            Icons.Default.PlayArrow,

                    contentDescription =
                        if (state.isPlaying)
                            "Pauza"
                        else
                            "Odtwórz"
                )
            }

            IconButton(
                onClick = {
                    viewModel.stop()
                }
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Stop,

                    contentDescription =
                        "Stop"
                )
            }
        }
    }
}