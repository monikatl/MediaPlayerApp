package com.baszczyk.mediaplayerapp.sreens.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.baszczyk.mediaplayerapp.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaPlayerScreen(
    songId: Long? = null,
    viewModel: MediaPlayerViewModel = koinViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(songId) {
        if (songId != null) {
            viewModel.playSongById(songId)
        }
    }

    val song = state.currentSong

    val backgroundColor = Color(0xFFF2FAFF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        if (song == null) {

            Text(
                text = "Brak utworu",
                modifier = Modifier.align(
                    Alignment.Center
                ),
                style = MaterialTheme.typography.titleLarge
            )

            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 20.dp
                ),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp),

                shape = RoundedCornerShape(28.dp),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                AsyncImage(
                    model = song.song.imageUrl
                        ?: R.drawable.note,

                    contentDescription = song.song.name,

                    modifier = Modifier.fillMaxSize(),

                    contentScale = ContentScale.Crop
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = song.song.name,

                        style = MaterialTheme
                            .typography
                            .headlineSmall,

                        fontWeight = FontWeight.Bold,

                        maxLines = 1
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = song.author.name,

                        style = MaterialTheme
                            .typography
                            .bodyLarge,

                        color = MaterialTheme
                            .colorScheme
                            .onSurfaceVariant,

                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = {
                        // tutaj później favorite
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.FavoriteBorder,

                        contentDescription =
                            "Dodaj do ulubionych",

                        tint = MaterialTheme
                            .colorScheme
                            .primary
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )


            Slider(
                value = 0f,
                onValueChange = {
                    // tutaj później seek
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "0:00",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "0:00",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        // TODO seek -10
                    },
                    modifier = Modifier.size(52.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Cofnij 10 sekund",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme
                                .colorScheme
                                .primary
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    IconButton(
                        onClick = {

                            if (state.isPlaying) {
                                viewModel.pause()
                            } else {
                                viewModel.resume()
                            }
                        },

                        modifier = Modifier.fillMaxSize()
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
                                    "Odtwórz",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(16.dp)
                )


                IconButton(
                    onClick = {
                        // TODO seek +10
                    },
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Przewiń 10 sekund",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            IconButton(
                onClick = {
                    viewModel.stop()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Zatrzymaj",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}