package com.example.bluetoothkmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.bluetoothkmm.android.ui.screens.NavGraphs
import com.example.bluetoothkmm.android.ui.theme.SimpleNavComposeAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
private fun MainScreen() {
    SimpleNavComposeAppTheme {
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}

