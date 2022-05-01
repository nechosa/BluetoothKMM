package com.example.bluetoothkmm.android.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bluetoothkmm.viewmodels.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.bluefalcon.BluetoothPeripheral
import dev.icerock.moko.mvvm.livedata.asFlow
import org.koin.androidx.compose.get

@Destination
@Composable
fun BluetoothDetailsScreen(
    navigator: DestinationsNavigator,
    mainViewModel: MainViewModel = get()
) {
    val currentDevice by mainViewModel.currentSelectedDeviceFlow.asFlow()
        .collectAsState(initial = null)

    BackHandler {
        onBackPressed(currentDevice, mainViewModel, navigator)
    }
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { onBackPressed(currentDevice, mainViewModel, navigator) }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
            Text(
                text = "Device Details",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }) {
        Column(modifier = Modifier.fillMaxSize()) {
            currentDevice?.let { nnCurrentDevice ->
                Text(
                    text = "Device name: ${nnCurrentDevice.name}",
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "UUID: ${nnCurrentDevice.uuid}", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "RSSI: ${nnCurrentDevice.rssi}", style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.padding(8.dp))
                nnCurrentDevice.services.forEachIndexed { index, service ->
                    Text(
                        text = "Service ${index + 1}: ${service.name}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

private fun onBackPressed(
    currentDevice: BluetoothPeripheral?,
    mainViewModel: MainViewModel,
    navigator: DestinationsNavigator
) {
    currentDevice?.let { nnCurrentDevice ->
        mainViewModel.disconnectFromDevice(nnCurrentDevice)
        navigator.navigateUp()
    } ?: navigator.navigateUp()
}
