package com.example.bluetoothkmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.example.bluetoothkmm.android.ui.screens.NavGraphs
import com.example.bluetoothkmm.android.ui.theme.SimpleNavComposeAppTheme
import com.example.bluetoothkmm.android.ui.utils.askForBluetoothPermissions
import com.example.bluetoothkmm.android.ui.utils.launchLifecycleScope
import com.example.bluetoothkmm.viewmodels.MainViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import dev.icerock.moko.mvvm.livedata.asFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askForBluetoothPermissions(mainViewModel)

        setContent {
            MainScreen()
        }

        launchLifecycleScope {
            mainViewModel.permissionsFlow.asFlow().collectLatest { arePermissionsGranted ->
                if (arePermissionsGranted) {
                    mainViewModel.searchDevices(applicationContext = application)
                }
            }
        }
    }
}

@Composable
private fun MainScreen() {
    SimpleNavComposeAppTheme {
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}

