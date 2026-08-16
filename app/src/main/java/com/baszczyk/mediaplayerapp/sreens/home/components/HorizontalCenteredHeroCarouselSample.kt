package com.baszczyk.mediaplayerapp.sreens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.baszczyk.mediaplayerapp.R
import com.baszczyk.mediaplayerapp.models.SongWithState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalCenteredHeroCarouselSample(
    songs: List<SongWithState>,
    onSongClick: (SongWithState) -> Unit
) {

    if (songs.isEmpty()) return

    val carouselState = rememberCarouselState {
        songs.size
    }

    HorizontalCenteredHeroCarousel(
        state = carouselState,
        modifier = Modifier
            .height(200.dp),
        maxItemWidth = 280.dp,
        itemSpacing = 12.dp,
        contentPadding = PaddingValues(
            horizontal = 24.dp
        ),
        minSmallItemWidth = 70.dp,
        maxSmallItemWidth = 110.dp,
        flingBehavior =
            CarouselDefaults.singleAdvanceFlingBehavior(
                state = carouselState
            )
    ) { index ->

        val song = songs[index]

        Card(
            onClick = { onSongClick(song) },
            modifier = Modifier
                .height(310.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFFFF)
            )
        ) {

            Column {
                AsyncImage(
                    model = song.song.imageUrl
                        ?: R.drawable.note,
                    contentDescription = song.song.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),

                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = song.song.name,
                        style = MaterialTheme.typography.titleSmall,
                    )

                    Text(
                        text = song.author.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}