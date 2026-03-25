package com.example.safevault

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.safevault.di.AppContainer
import com.example.safevault.navigation.SafeVaultNavHost

@Composable
fun SafeVaultApp(
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        SafeVaultNavHost(
            navController = navController,
            appContainer = appContainer,
        )
    }
}
