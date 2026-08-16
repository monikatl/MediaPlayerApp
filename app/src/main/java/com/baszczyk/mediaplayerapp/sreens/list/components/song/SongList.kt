package com.baszczyk.mediaplayerapp.sreens.list.components.song

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.models.SongWithState

@Composable
fun SongList(
    songs: List<SongWithState>,
    onSongPlayClick: (SongWithState) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(
            items = songs,
            key = { item ->
                item.song.id
            }
        ) { item ->

            SongItem(
                songWithState = item,
                onSongPlayClick = {
                    onSongPlayClick(item)
                }
            )

            HorizontalDivider()
        }
    }
}