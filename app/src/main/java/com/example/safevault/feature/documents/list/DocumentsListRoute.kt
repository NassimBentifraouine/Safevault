package com.example.safevault.feature.documents.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safevault.data.repository.DocumentsRepository

@Composable
fun DocumentsListRoute(
    documentsRepository: DocumentsRepository,
    onAddDocumentClick: () -> Unit,
    onDocumentClick: (Long) -> Unit,
) {
    val viewModel: DocumentsListViewModel = viewModel(
        factory = DocumentsListViewModel.provideFactory(
            documentsRepository = documentsRepository,
        ),
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DocumentsListScreen(
        uiState = uiState,
        onAddDocumentClick = onAddDocumentClick,
        onDocumentClick = onDocumentClick,
        onCategoryFilterChange = viewModel::onCategoryFilterChange,
        onExpirationFilterChange = viewModel::onExpirationFilterChange,
        onSortOptionChange = viewModel::onSortOptionChange,
        onResetFiltersClick = viewModel::onResetFiltersClick,
    )
}
