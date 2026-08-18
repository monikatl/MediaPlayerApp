package com.baszczyk.mediaplayerapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.baszczyk.mediaplayerapp.components.BottomBar
import com.baszczyk.mediaplayerapp.components.CustomTopAppBar
import com.baszczyk.mediaplayerapp.data.supabase.SupabaseProvider
import io.github.jan.supabase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var userName by remember {
        mutableStateOf("Użytkownik")
    }

    LaunchedEffect(Unit) {
        val user = SupabaseProvider.client.auth.currentUserOrNull()

        userName = user
            ?.userMetadata
            ?.get("name")
            ?.toString()
            ?.replace("\"", "")
            ?: user?.email
                    ?: "Użytkownik"
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                userName = userName,
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(
                currentRoute = currentRoute,
                navController = navController
            )
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            paddingValues = paddingValues
        )
    }
}