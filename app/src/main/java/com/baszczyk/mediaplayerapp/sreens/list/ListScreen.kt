package com.baszczyk.mediaplayerapp.sreens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.baszczyk.mediaplayerapp.components.EmptyContent
import com.baszczyk.mediaplayerapp.components.ErrorContent
import com.baszczyk.mediaplayerapp.components.LoadingContent
import com.baszczyk.mediaplayerapp.models.Song
import com.baszczyk.mediaplayerapp.sreens.list.components.SearchTypeSegmentedButton
import com.baszczyk.mediaplayerapp.sreens.list.components.author.AuthorItem
import com.baszczyk.mediaplayerapp.sreens.list.components.song.FilterChips
import com.baszczyk.mediaplayerapp.sreens.list.components.song.SongList
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListScreen(
    viewModel: ListViewModel = koinViewModel(),
    onSongPlayClick: (Song) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var isSearchVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedFilter by remember {
        mutableIntStateOf(0)
    }

    var selectedSort by remember {
        mutableIntStateOf(Filter.AUTHOR)
    }

    val filteredSongs = remember(
        state.songs,
        searchQuery,
        selectedFilter,
        selectedSort
    ) {
        state.songs
            .filter { item ->

                when (selectedFilter) {
                    0 -> true
                    1 -> item.state.isFavorite
                    2 -> item.state.isListened
                    else -> true
                }
            }
            .filter { item ->

                val query = searchQuery.trim()

                if (query.isEmpty()) {
                    true
                } else {

                    when (selectedSort) {

                        Filter.AUTHOR -> {
                            item.author.name.contains(
                                query,
                                ignoreCase = true
                            )
                        }

                        Filter.TITLE -> {
                            item.song.name.contains(
                                query,
                                ignoreCase = true
                            )
                        }
                        else -> true
                    }
                }
            }
    }

    val songsByAuthor = remember(filteredSongs) {

        filteredSongs.groupBy {
            it.author.id
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedSort == Filter.AUTHOR) {
                    "Autorzy"
                } else {
                    "Utwory"
                },
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {

                    isSearchVisible = !isSearchVisible

                    if (!isSearchVisible) {
                        searchQuery = ""
                    }
                }
            ) {

                Icon(
                    imageVector = if (isSearchVisible) {
                        Icons.Default.Close
                    } else {
                        Icons.Default.Search
                    },
                    contentDescription = if (isSearchVisible) {
                        "Zamknij wyszukiwanie"
                    } else {
                        "Szukaj"
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = isSearchVisible
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                singleLine = true,
                label = {
                    Text("Szukaj")
                },
                placeholder = {
                    Text(
                        if (selectedSort == Filter.AUTHOR) {
                            "Szukaj po autorze"
                        } else {
                            "Szukaj po tytule"
                        }
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {

                        IconButton(
                            onClick = {
                                searchQuery = ""
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Wyczyść"
                            )
                        }
                    }
                }
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        SearchTypeSegmentedButton(
            selectedSort = selectedSort,

            onSortSelected = {
                selectedSort = it
                searchQuery = ""

                if (it == Filter.AUTHOR) {
                    selectedFilter = 0
                }
            }
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        if (selectedSort == Filter.TITLE) {

            FilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }


        when {

            state.isLoading -> {

                LoadingContent()
            }

            state.error != null -> {

                ErrorContent(
                    label = "Coś poszło nie tak..."
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

            selectedSort == Filter.AUTHOR -> {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    songsByAuthor.forEach { (_, songs) ->

                        val author = songs
                            .first()
                            .author

                        AuthorItem(
                            author = author,
                            songs = songs,
                            onSongPlayClick = onSongPlayClick
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                    }
                }
            }

            selectedSort == Filter.TITLE -> {
                SongList(
                    songs = filteredSongs,
                    onSongPlayClick = { onSongPlayClick(it.song) }
                )
            }
        }
    }
}