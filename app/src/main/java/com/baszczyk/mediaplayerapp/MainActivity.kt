package com.baszczyk.mediaplayerapp

import android.os.Bundle
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
                AppRoot()
            }
        }
    }
}