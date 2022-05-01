package com.example.bluetoothkmm.di

import com.example.bluetoothkmm.viewmodels.MainViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules()
}

fun initKoin() = initKoin {}

val commonModule = module {
    single { MainViewModel() }
}