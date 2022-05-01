package com.example.bluetoothkmm.android.ui.utils

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetoothkmm.viewmodels.MainViewModel

fun AppCompatActivity.askForBluetoothPermissions(
    mainViewModel: MainViewModel
) {
    val btPerms =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionResult ->
            var isGranted = false
            permissionResult.forEach { (_, isPermissionGranted) ->
                isGranted = isPermissionGranted
            }
            mainViewModel.setPermission(isGranted)
        }
    val listOfPerms = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    // Permissions to ask only on A12
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
        ).forEach { permission ->
            listOfPerms.add(permission)
        }
    }

    btPerms.launch(listOfPerms.toTypedArray())
}
