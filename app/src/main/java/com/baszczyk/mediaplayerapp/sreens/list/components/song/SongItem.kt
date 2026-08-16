package com.baszczyk.mediaplayerapp.sreens.list.components.song

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baszczyk.mediaplayerapp.R
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.models.SongWithState

@Composable
fun SongItem(
    songWithState: SongWithState,
    onSongPlayClick: (Song) -> Unit
) {
    val song = songWithState.song
    val author = songWithState.author
    val state = songWithState.state

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
                text = author.name,
                fontSize = 10.sp,
                color = Color(0xFF003153)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.PlayCircleOutline,
                contentDescription = "Odtwórz",
                tint = Color(0xFF003153),
                modifier = Modifier
                    .clickable {
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