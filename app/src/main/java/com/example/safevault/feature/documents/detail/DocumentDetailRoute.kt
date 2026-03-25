package com.example.safevault.feature.documents.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safevault.data.repository.DocumentsRepository

@Composable
fun DocumentDetailRoute(
    documentId: Long,
    documentsRepository: DocumentsRepository,
    onBackClick: () -> Unit,
    onDocumentDeleted: () -> Unit,
) {
    val viewModel: DocumentDetailViewModel = viewModel(
        factory = DocumentDetailViewModel.provideFactory(
            documentId = documentId,
            documentsRepository = documentsRepository,
        ),
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            if (event is DocumentDetailEvent.Deleted) {
                onDocumentDeleted()
            }
        }
    }

    DocumentDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onDeleteConfirm = viewModel::onDeleteConfirm,
    )
}
