package com.baszczyk.mediaplayerapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.baszczyk.mediaplayerapp.sreens.home.HomeScreen
import com.baszczyk.mediaplayerapp.sreens.list.ListScreen
import com.baszczyk.mediaplayerapp.sreens.mediaplayer.MediaPlayerScreen

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
            HomeScreen()
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
                onSongPlayClick =  { song ->
                    navController.navigate(
                        "mediaplayer/${song.id}"
                    )
                }
            )
        }
    }
}