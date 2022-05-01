package com.example.bluetoothkmm.android

import android.app.Application
import com.example.bluetoothkmm.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BluetoothKMMApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            //androidLogger()
            androidContext(this@BluetoothKMMApplication)
            //androidContext()
            modules(commonModule)
        }
    }
}