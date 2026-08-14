package com.baszczyk.mediaplayerapp.sreens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val message: String = "Witaj!"
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun changeMessage() {
        _uiState.value = HomeUiState(
            message = "Zmieniono wiadomość!"
        )
    }
}