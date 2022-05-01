package com.example.bluetoothkmm.viewmodels

import android.util.Log
import dev.bluefalcon.ApplicationContext
import dev.bluefalcon.BlueFalcon
import dev.bluefalcon.BlueFalconDelegate
import dev.bluefalcon.BluetoothCharacteristic
import dev.bluefalcon.BluetoothCharacteristicDescriptor
import dev.bluefalcon.BluetoothPeripheral
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _permissionsFlow: MutableLiveData<Boolean> = MutableLiveData(initialValue = false)
    val permissionsFlow: LiveData<Boolean> = _permissionsFlow

    private val _devicesFlow: MutableLiveData<BluetoothPeripheral?> = MutableLiveData(null)
    val devicesFlow: LiveData<BluetoothPeripheral?> = _devicesFlow

    private val _isScanningFlow: MutableLiveData<Boolean> = MutableLiveData(initialValue = false)
    val isScanningFlow: LiveData<Boolean> = _isScanningFlow

    private val _currentSelectedDeviceFlow: MutableLiveData<BluetoothPeripheral?> =
        MutableLiveData(null)
    val currentSelectedDeviceFlow: LiveData<BluetoothPeripheral?> = _currentSelectedDeviceFlow

    var blueFalcon: BlueFalcon? = null

    fun setPermission(isPermissionGranted: Boolean) {
        _permissionsFlow.postValue(isPermissionGranted)
    }

    fun searchDevices(applicationContext: ApplicationContext) {
        blueFalcon = BlueFalcon(applicationContext, null)
        blueFalcon?.apply {
            startScanningForDevices()
            delegates.add(
                object : BlueFalconDelegate {
                    override fun didCharacteristcValueChanged(
                        bluetoothPeripheral: BluetoothPeripheral,
                        bluetoothCharacteristic: BluetoothCharacteristic
                    ) {
                        Log.d(
                            TAG,
                            "didCharacteristcValueChanged: yes! which device? : ${bluetoothPeripheral.uuid}"
                        )
                    }

                    override fun didConnect(bluetoothPeripheral: BluetoothPeripheral) {
                        Log.d(TAG, "didConnect: yes! which device? : ${bluetoothPeripheral.uuid}")
                        _currentSelectedDeviceFlow.postValue(bluetoothPeripheral)
                    }

                    override fun didDisconnect(bluetoothPeripheral: BluetoothPeripheral) {
                        Log.d(
                            TAG,
                            "didDisconnect: yes! which device? : ${bluetoothPeripheral.uuid}"
                        )
                    }

                    override fun didDiscoverCharacteristics(bluetoothPeripheral: BluetoothPeripheral) {
                        Log.d(
                            TAG,
                            "didDiscoverCharacteristics: yes! which device? : ${bluetoothPeripheral.uuid}"
                        )
                        _currentSelectedDeviceFlow.postValue(bluetoothPeripheral)
                    }

                    override fun didDiscoverDevice(bluetoothPeripheral: BluetoothPeripheral) {
                        _devicesFlow.postValue(bluetoothPeripheral)
                    }

                    override fun didDiscoverServices(bluetoothPeripheral: BluetoothPeripheral) {
                        Log.d(
                            TAG,
                            "didDiscoverServices: yes! for which device: ${bluetoothPeripheral.name}"
                        )
                        _currentSelectedDeviceFlow.postValue(bluetoothPeripheral)
                    }

                    override fun didReadDescriptor(
                        bluetoothPeripheral: BluetoothPeripheral,
                        bluetoothCharacteristicDescriptor: BluetoothCharacteristicDescriptor
                    ) {
                        Log.d(
                            TAG,
                            "didReadDescriptor: yes! which device? : ${bluetoothPeripheral.uuid}"
                        )
                    }

                    override fun didRssiUpdate(bluetoothPeripheral: BluetoothPeripheral) {
                        Log.d(
                            TAG,
                            "didRssiUpdate: yes! for which device? : ${bluetoothPeripheral.name}"
                        )
                        _currentSelectedDeviceFlow.postValue(bluetoothPeripheral)
                    }

                    override fun didUpdateMTU(bluetoothPeripheral: BluetoothPeripheral) {
                        Log.d(TAG, "didUpdateMTU: yes! which device? : ${bluetoothPeripheral.uuid}")
                    }
                }
            )
        }
    }

    fun connectToDevice(bluetoothPeripheral: BluetoothPeripheral) {
        blueFalcon?.apply {
            stopScanningForDevices()
            if (!isScanning) {
                connect(bluetoothPeripheral = bluetoothPeripheral)
            }
        }
    }

    fun disconnectFromDevice(bluetoothPeripheral: BluetoothPeripheral) {
        blueFalcon?.apply {
            disconnect(bluetoothPeripheral)
            _currentSelectedDeviceFlow.postValue(null)
            startScanningForDevices()
        }
    }

    private fun BlueFalcon.startScanningForDevices() {
        scan()
        if (isScanning) {
            _isScanningFlow.postValue(true)
        }
    }

    private fun BlueFalcon.stopScanningForDevices() {
        stopScanning()
        if (!isScanning) {
            _isScanningFlow.postValue(false)
        }
    }
}
