package com.example.safevault.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.safevault.di.AppContainer
import com.example.safevault.feature.documents.add.AddDocumentRoute
import com.example.safevault.feature.documents.detail.DocumentDetailRoute
import com.example.safevault.feature.documents.edit.EditDocumentRoute
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
                onDocumentClick = { documentId ->
                    navController.navigate(
                        SafeVaultDestination.detailRoute(documentId = documentId),
                    )
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

        composable(
            route = SafeVaultDestination.DOCUMENT_DETAIL_ROUTE,
            arguments = listOf(
                navArgument("documentId") { type = NavType.LongType },
            ),
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getLong("documentId") ?: 0L
            DocumentDetailRoute(
                documentId = documentId,
                documentsRepository = appContainer.documentsRepository,
                onBackClick = { navController.popBackStack() },
                onDocumentDeleted = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(SafeVaultDestination.editRoute(documentId = documentId))
                },
            )
        }

        composable(
            route = SafeVaultDestination.EDIT_DOCUMENT_ROUTE,
            arguments = listOf(
                navArgument("documentId") { type = NavType.LongType },
            ),
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getLong("documentId") ?: 0L
            EditDocumentRoute(
                documentId = documentId,
                documentsRepository = appContainer.documentsRepository,
                onBackClick = { navController.popBackStack() },
                onDocumentUpdated = { navController.popBackStack() },
            )
        }
    }
}
