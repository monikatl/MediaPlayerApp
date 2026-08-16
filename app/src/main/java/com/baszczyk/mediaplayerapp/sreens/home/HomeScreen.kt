package com.baszczyk.mediaplayerapp.sreens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baszczyk.mediaplayerapp.components.ErrorContent
import com.baszczyk.mediaplayerapp.components.LoadingContent
import com.baszczyk.mediaplayerapp.models.SongWithState
import com.baszczyk.mediaplayerapp.sreens.home.components.HorizontalCenteredHeroCarouselSample
import com.baszczyk.mediaplayerapp.ui.home.SongItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onSongClick: (SongWithState) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHome()
    }

    when {
        uiState.isLoading -> {
            LoadingContent()
        }

        uiState.error != null -> {
            ErrorContent(
                "Coś poszło nie tak..."
            )
        }

        else -> {
            HomeContent(
                newSongs = uiState.newSongs,
                recentlyListened = uiState.recentlyListened,
                onSongClick = onSongClick
            )
        }
    }
}

@Composable
private fun HomeContent(
    newSongs: List<SongWithState>,
    recentlyListened: List<SongWithState>,
    onSongClick: (SongWithState) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 24.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (newSongs.isNotEmpty()) {

            item {
                Text(
                    text = "Nowe piosenki",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    )
                )
                HorizontalCenteredHeroCarouselSample(songs = newSongs) {

                }
            }

        }


        if (newSongs.isNotEmpty()) {

            item {
                Text(
                    text = "Ostatnio słuchane",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 8.dp
                    )
                )
            }

            items(
                items = newSongs,
                key = { songWithState ->
                    songWithState.song.id
                }
            ) { songWithState ->

                SongItem(
                    song = songWithState,
                    onClick = {
                        onSongClick(songWithState)
                    },
                    modifier = Modifier.padding(
                        horizontal = 10.dp
                    )
                )
            }
        }

        if (
            newSongs.isEmpty() &&
            recentlyListened.isEmpty()
        ) {
            item {
                Text(
                    text = "Brak piosenek do wyświetlenia.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    )
                )
            }
        }
    }
}