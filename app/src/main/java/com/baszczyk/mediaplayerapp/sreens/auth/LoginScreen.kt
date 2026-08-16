package com.baszczyk.mediaplayerapp.sreens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Media Player"
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::updateEmail,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Email")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::updatePassword,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Hasło")
            },
            singleLine = true,
            visualTransformation =
                PasswordVisualTransformation()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        state.error?.let { error ->

            Text(
                text = error
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        Button(
            onClick = {
                viewModel.login()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {

            if (state.isLoading) {

                CircularProgressIndicator()

            } else {

                Text("Zaloguj się")
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TextButton(
            onClick = onRegisterClick
        ) {

            Text(
                text = "Nie masz konta? Zarejestruj się"
            )
        }
    }
}