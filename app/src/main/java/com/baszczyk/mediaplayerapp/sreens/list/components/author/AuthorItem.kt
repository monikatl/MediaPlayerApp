package com.baszczyk.mediaplayerapp.sreens.list.components.author

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.baszczyk.mediaplayerapp.models.Author
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.models.SongWithState
import com.baszczyk.mediaplayerapp.sreens.list.components.song.SongItem
import com.baszczyk.mediaplayerapp.R

@Composable
fun AuthorItem(
    author: Author,
    songs: List<SongWithState>,
    onSongPlayClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = if(author.imageUrl?.isEmpty() == true) R.drawable.author_placeholder else author.imageUrl,
                    contentDescription = author.name,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = author.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "${songs.size} utworów",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {

                    Icon(
                        imageVector = if (expanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = if (expanded) {
                            "Zwiń"
                        } else {
                            "Rozwiń"
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    songs.forEach { item ->

                        SongItem(
                           songWithState = item,
                            onSongPlayClick = {
                                onSongPlayClick(item.song)
                            }
                        )
                    }
                }
            }
        }
    }
}