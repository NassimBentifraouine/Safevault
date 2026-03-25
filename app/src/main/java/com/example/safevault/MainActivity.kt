package com.example.safevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.safevault.ui.theme.SafeVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = (application as SafeVaultApplication).container

        setContent {
            SafeVaultTheme {
                SafeVaultApp(appContainer = appContainer)
            }
        }
    }
}
