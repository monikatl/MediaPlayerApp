package com.baszczyk.mediaplayerapp.sreens.list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.baszczyk.mediaplayerapp.models.Song

@Composable
fun SongList(
    songs: List<Song>,
    onSongPlayClick: (Song) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(
            items = songs,
            key = { song -> song.id }
        ) { song ->

            SongItem(
                song = song,
                onSongPlayClick = onSongPlayClick
            )

            HorizontalDivider()
        }
    }
}