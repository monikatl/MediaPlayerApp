package com.baszczyk.mediaplayerapp.sreens.home


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Text(text = uiState.message)

    Button(
        onClick = {
            viewModel.changeMessage()
        }
    ) {
        Text("Zmień")
    }
}