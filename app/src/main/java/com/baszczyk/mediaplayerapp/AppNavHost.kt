package com.baszczyk.mediaplayerapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.baszczyk.mediaplayerapp.repo.AuthRepository
import com.baszczyk.mediaplayerapp.sreens.home.HomeScreen
import com.baszczyk.mediaplayerapp.sreens.list.ListScreen
import com.baszczyk.mediaplayerapp.sreens.auth.LoginScreen
import com.baszczyk.mediaplayerapp.sreens.mediaplayer.MediaPlayerScreen
import com.baszczyk.mediaplayerapp.sreens.auth.RegisterScreen
import com.baszczyk.mediaplayerapp.sreens.profile.ProfileScreen
import com.baszczyk.mediaplayerapp.sreens.profile.ProfileViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues)
    ) {

        composable("home") {
            HomeScreen(
                {}
            )
        }

        composable("mediaplayer") {
            MediaPlayerScreen()
        }

        composable(
            route = "mediaplayer/{songId}"
        ) { backStackEntry ->

            val songId = backStackEntry
                .arguments
                ?.getString("songId")
                ?.toLongOrNull()

            MediaPlayerScreen(
                songId = songId
            )
        }

        composable("list") {
            ListScreen(
                onSongPlayClick = { song ->
                    navController.navigate(
                        "mediaplayer/${song.id}"
                    )
                }
            )
        }

        composable("profile") {
            val authRepository: AuthRepository = koinInject()
            val profileViewModel: ProfileViewModel = koinViewModel()

            val user = authRepository.currentUser()

            if (user != null) {
                ProfileScreen(
                    userId = user.id,
                    viewModel = profileViewModel
                )
            }
        }
    }
}