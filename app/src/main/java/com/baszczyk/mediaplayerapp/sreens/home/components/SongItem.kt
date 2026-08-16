package com.baszczyk.mediaplayerapp.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.baszczyk.mediaplayerapp.R
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.models.SongWithState

@Composable
fun SongItem(
    song: SongWithState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = song.song.imageUrl ?: R.drawable.note,
                contentDescription = song.song.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            androidx.compose.foundation.layout.Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.song.name,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = song.author.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}