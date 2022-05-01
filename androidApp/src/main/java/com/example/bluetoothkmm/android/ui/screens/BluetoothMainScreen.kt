package com.example.bluetoothkmm.android.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.bluefalcon.BluetoothPeripheral
import com.example.bluetoothkmm.viewmodels.MainViewModel
import com.example.bluetoothkmm.android.ui.screens.destinations.BluetoothDetailsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.icerock.moko.mvvm.livedata.asFlow
import org.koin.androidx.compose.get

//@RootNavGraph(start = true)
@Destination
@Composable
fun BluetoothMainScreen(
    navigator: DestinationsNavigator,
    mainViewModel: MainViewModel = get()
) {
    val ctx = LocalContext.current
    val device by mainViewModel.devicesFlow.asFlow()
        .collectAsState(initial = null)
    val devices by remember {
        mutableStateOf(
            mutableListOf<BluetoothPeripheral>()
        )
    }
    addOrUpdateDevicesInList(device, devices)
    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenHeader(mainViewModel = mainViewModel)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                devices.distinctBy { device -> device.uuid }.forEach { device ->
                    item {
                        ctx // todo: check why this context is needed to display the DeviceCard
                        DeviceCard(
                            device = device,
                            mainViewModel = mainViewModel,
                            navigator = navigator
                        )
                    }
                }
            }
        }
    }
}

private fun addOrUpdateDevicesInList(
    device: BluetoothPeripheral?,
    devices: MutableList<BluetoothPeripheral>
) {
    device?.let { nnDevice ->
        val indexOfExistingElement = devices.indexOf(nnDevice)
        if (indexOfExistingElement != -1) {
            devices[indexOfExistingElement] = nnDevice
        } else {
            devices.add(nnDevice)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ScreenHeader(
    mainViewModel: MainViewModel
) {
    val isScanning by mainViewModel.isScanningFlow.asFlow().collectAsState(initial = false)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "List of devices nearby:",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp)
        )
        AnimatedVisibility(visible = isScanning) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp),
                strokeWidth = 2.dp
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DeviceCard(
    device: BluetoothPeripheral,
    mainViewModel: MainViewModel,
    navigator: DestinationsNavigator
) {
    val currentDevice by mainViewModel.currentSelectedDeviceFlow.asFlow()
        .collectAsState(initial = null)

    currentDevice?.let {
        navigator.navigate(BluetoothDetailsScreenDestination)
    }

    var isConnecting by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(percent = 20)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = device.name.orEmpty(),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = "UUID: ${device.uuid}",
                    style = MaterialTheme.typography.caption
                )
            }
            Column(
                modifier = Modifier.padding(end = 24.dp)
            ) {
                AnimatedVisibility(visible = !isConnecting) {
                    IconButton(
                        onClick = {
                            isConnecting = true
                            mainViewModel.connectToDevice(device)
                        },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = com.example.bluetoothkmm.android.R.drawable.ic_circle_right_arrow),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
                AnimatedVisibility(visible = isConnecting) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp),
                        strokeWidth = 1.dp
                    )
                }
                Text(
                    text = if (isConnecting) "Connecting..." else "Connect",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}
