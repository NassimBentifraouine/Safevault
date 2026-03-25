package com.example.safevault

import android.app.Application
import com.example.safevault.di.AppContainer
import com.example.safevault.di.DefaultAppContainer

class SafeVaultApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = this)
    }
}
