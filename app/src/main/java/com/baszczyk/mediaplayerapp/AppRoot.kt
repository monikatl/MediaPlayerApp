package com.baszczyk.mediaplayerapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.baszczyk.mediaplayerapp.sreens.auth.AuthViewModel
import com.baszczyk.mediaplayerapp.sreens.auth.LoginScreen
import com.baszczyk.mediaplayerapp.sreens.auth.RegisterScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppRoot(
    authViewModel: AuthViewModel = koinViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()

    if (authState.isLoggedIn) {

        MainScreen(
//            onLogout = {
//                authViewModel.logout()
//            }
        )

    } else {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "login"
        ) {

            composable("login") {

                LoginScreen(
                    onRegisterClick = {
                        navController.navigate("register")
                    }
                )
            }

            composable("register") {

                RegisterScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}