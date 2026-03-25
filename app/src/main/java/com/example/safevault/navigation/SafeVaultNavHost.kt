package com.example.safevault.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.safevault.di.AppContainer
import com.example.safevault.feature.documents.add.AddDocumentRoute
import com.example.safevault.feature.documents.list.DocumentsListRoute

@Composable
fun SafeVaultNavHost(
    navController: NavHostController,
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SafeVaultDestination.DOCUMENTS_LIST_ROUTE,
        modifier = modifier,
    ) {
        composable(route = SafeVaultDestination.DOCUMENTS_LIST_ROUTE) {
            DocumentsListRoute(
                documentsRepository = appContainer.documentsRepository,
                onAddDocumentClick = {
                    navController.navigate(SafeVaultDestination.ADD_DOCUMENT_ROUTE)
                },
            )
        }

        composable(route = SafeVaultDestination.ADD_DOCUMENT_ROUTE) {
            AddDocumentRoute(
                documentsRepository = appContainer.documentsRepository,
                onBackClick = { navController.popBackStack() },
                onDocumentSaved = { navController.popBackStack() },
            )
        }
    }
}
