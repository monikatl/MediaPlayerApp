package com.baszczyk.mediaplayerapp.sreens.list.components

import com.baszczyk.mediaplayerapp.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baszczyk.mediaplayerapp.models.Song

@Composable
fun SongItem(
    song: Song,
    onSongPlayClick: (Song) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = song.imageUrl ?: R.drawable.note,
            contentDescription = song.name,
            modifier = Modifier
                .size(64.dp)
                .clip(
                    RoundedCornerShape(8.dp)
                ),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy((-6).dp)
        ) {

            Text(
                text = song.name,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )

            Text(
                text = song.author,
                fontSize = 10.sp,
                color = Color(0xFF003153),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircleOutline,
                contentDescription = "play",
                tint = Color(0xFF003153),
                modifier = Modifier
                    .clickable{
                        onSongPlayClick(song)
                    }
                    .size(28.dp)
            )
            Text(
                text = formatDuration(song.duration),
                fontSize = 10.sp
            )
        }
    }
}

private fun formatDate(
    date: String
): String {
    return date.take(10)
}

private fun formatDuration(
    durationMs: Long
): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "%d:%02d".format(
        minutes,
        seconds
    )
}

@Preview(showBackground = true)
@Composable
private fun SongItemPreview() {
    SongItem(
        song = Song(
            id = 1L,
            name = "Blinding Lights",
            author = "The Weeknd",
            createdAt = "2026-08-14",
            duration = 200_000L,
            imageUrl = null,
            uri = "",
            storagePath = "",
            isFavorite = true,
            isListened = true,
        ),
        onSongPlayClick = {}
    )
}