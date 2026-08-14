package com.baszczyk.mediaplayerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.baszczyk.mediaplayerapp.ui.theme.MediaPlayerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaPlayerAppTheme {
                MainScreen()
            }
        }
        Log.d(
            "SUPABASE",
            "URL = ${BuildConfig.SUPABASE_URL}"
        )
    }
}