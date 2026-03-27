package com.example.safevault.feature.documents.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safevault.data.repository.DocumentsRepository

@Composable
fun EditDocumentRoute(
    documentId: Long,
    documentsRepository: DocumentsRepository,
    onBackClick: () -> Unit,
    onDocumentUpdated: () -> Unit,
    onUpdateFailed: () -> Unit,
) {
    val viewModel: EditDocumentViewModel = viewModel(
        factory = EditDocumentViewModel.provideFactory(
            documentId = documentId,
            documentsRepository = documentsRepository,
        ),
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is EditDocumentEvent.Updated -> onDocumentUpdated()
                is EditDocumentEvent.UpdateFailed -> onUpdateFailed()
            }
        }
    }

    EditDocumentScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSaveClick,
        onTitleChange = viewModel::onTitleChange,
        onCategoryChange = viewModel::onCategoryChange,
        onNoteChange = viewModel::onNoteChange,
        onImagePicked = viewModel::onImagePicked,
    )
}
