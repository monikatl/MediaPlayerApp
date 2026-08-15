package com.baszczyk.mediaplayerapp.sreens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baszczyk.mediaplayerapp.components.EmptyContent
import com.baszczyk.mediaplayerapp.components.ErrorContent
import com.baszczyk.mediaplayerapp.components.LoadingContent
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.sreens.list.components.FilterChips
import com.baszczyk.mediaplayerapp.sreens.list.components.SearchTypeSegmentedButton
import org.koin.androidx.compose.koinViewModel
import com.baszczyk.mediaplayerapp.sreens.list.components.SongList

@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    onSongPlayClick: (Song) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedFilter by remember {
        mutableIntStateOf(0)
    }

    var selectedSort by remember {
        mutableIntStateOf(0)
    }

    val filteredSongs = remember(
        state.songs,
        searchQuery,
        selectedFilter,
        selectedSort
    ) {
        state.songs
            .filter { song ->
                when (selectedFilter) {
                    0 -> true
                    1 -> song.isFavorite
                    2 -> song.isListened
                    else -> true
                }
            }
            .filter { song ->
                val query = searchQuery.trim()

                if (query.isEmpty()) {
                    true
                } else {
                    when (selectedSort) {
                        0 -> song.author.contains(
                            query,
                            ignoreCase = true
                        )

                        1 -> song.name.contains(
                            query,
                            ignoreCase = true
                        )

                        else -> true
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Utwory",
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.padding(4.dp)
        )

        val textSize = 12.sp

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            singleLine = true,
            label = {
                Text(
                    text = "Szukaj",
                    fontSize = 10.sp
                )
            },
            placeholder = {
                Text(
                    if (selectedSort == 0) {
                        "Szukaj po autorze"
                    } else {
                        "Szukaj po tytule"
                    },
                    fontSize = textSize
                )
            },
        )

        Spacer(
            modifier = Modifier.padding(6.dp)
        )

        FilterChips(
            selectedFilter = selectedFilter,
            onFilterSelected = {
                selectedFilter = it
            }
        )

        Spacer(
            modifier = Modifier.padding(6.dp)
        )

        when {
            state.isLoading -> {
                LoadingContent()
            }

            state.error != null -> {
                ErrorContent(
                    message = state.error!!
                )
            }

            filteredSongs.isEmpty() -> {
                EmptyContent(
                    message = if (searchQuery.isNotBlank()) {
                        "Brak wyników"
                    } else {
                        "Brak piosenek"
                    }
                )
            }

            else -> {
                SongList(
                    songs = filteredSongs,
                    onSongPlayClick = onSongPlayClick
                )
            }
        }

        Spacer(
            modifier = Modifier.padding(6.dp)
        )

        SearchTypeSegmentedButton(
            selectedSort = selectedSort,
            onSortSelected = {
                selectedSort = it
            }
        )
    }
}