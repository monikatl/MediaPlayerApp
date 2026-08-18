package com.baszczyk.mediaplayerapp.sreens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    userId: String,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Profil",
            style = MaterialTheme.typography.headlineMedium
        )

        if (uiState.isLoading && !uiState.isEditing) {
            CircularProgressIndicator()
        }

        if (uiState.isEditing) {

            OutlinedTextField(
                value = uiState.username,
                onValueChange = viewModel::onUsernameChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Nazwa użytkownika")
                },
                singleLine = true,
                enabled = !uiState.isLoading
            )

            Button(
                onClick = {
                    viewModel.saveUsername(userId)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.username.isNotBlank() &&
                        !uiState.isLoading
            ) {
                Text(
                    text = if (uiState.isLoading) {
                        "Zapisywanie..."
                    } else {
                        "Zapisz"
                    }
                )
            }

            Button(
                onClick = viewModel::cancelEditing,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Anuluj")
            }

        } else {

            Text(
                text = "Nazwa użytkownika",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = uiState.username.ifBlank {
                    "Nie ustawiono nazwy użytkownika"
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = viewModel::startEditing,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text(
                    text = if (uiState.username.isBlank()) {
                        "Dodaj nazwę użytkownika"
                    } else {
                        "Edytuj nazwę użytkownika"
                    }
                )
            }
        }

        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}