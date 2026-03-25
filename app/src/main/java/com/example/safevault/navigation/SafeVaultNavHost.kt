package com.example.safevault.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.safevault.R
import com.example.safevault.di.AppContainer
import com.example.safevault.feature.documents.add.AddDocumentRoute
import com.example.safevault.feature.documents.detail.DocumentDetailRoute
import com.example.safevault.feature.documents.edit.EditDocumentRoute
import com.example.safevault.feature.documents.list.DocumentsListRoute
import kotlinx.coroutines.launch

@Composable
fun SafeVaultNavHost(
    navController: NavHostController,
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val messageSaved = stringResource(id = R.string.snackbar_document_saved)
    val messageSaveFailed = stringResource(id = R.string.snackbar_document_save_failed)
    val messageUpdated = stringResource(id = R.string.snackbar_document_updated)
    val messageUpdateFailed = stringResource(id = R.string.snackbar_document_update_failed)
    val messageDeleted = stringResource(id = R.string.snackbar_document_deleted)
    val messageDeleteFailed = stringResource(id = R.string.snackbar_document_delete_failed)

    fun showMessage(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        NavHost(
            navController = navController,
            startDestination = SafeVaultDestination.DOCUMENTS_LIST_ROUTE,
            modifier = Modifier.fillMaxSize(),
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
                    onDocumentSaved = {
                        showMessage(messageSaved)
                        navController.popBackStack()
                    },
                    onSaveFailed = { showMessage(messageSaveFailed) },
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
                    onDocumentDeleted = {
                        showMessage(messageDeleted)
                        navController.popBackStack()
                    },
                    onDeleteFailed = { showMessage(messageDeleteFailed) },
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
                    onDocumentUpdated = {
                        showMessage(messageUpdated)
                        navController.popBackStack()
                    },
                    onUpdateFailed = { showMessage(messageUpdateFailed) },
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        )
    }
}
