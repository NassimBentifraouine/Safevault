package com.example.safevault.feature.documents.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safevault.data.repository.DocumentsRepository

@Composable
fun AddDocumentRoute(
    documentsRepository: DocumentsRepository,
    onBackClick: () -> Unit,
    onDocumentSaved: () -> Unit,
    onSaveFailed: () -> Unit,
) {
    val viewModel: AddDocumentViewModel = viewModel(
        factory = AddDocumentViewModel.provideFactory(
            documentsRepository = documentsRepository,
        ),
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is AddDocumentEvent.Saved -> onDocumentSaved()
                is AddDocumentEvent.SaveFailed -> onSaveFailed()
            }
        }
    }

    AddDocumentScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSaveClick,
        onTitleChange = viewModel::onTitleChange,
        onCategoryChange = viewModel::onCategoryChange,
        onNoteChange = viewModel::onNoteChange,
        onImagePicked = viewModel::onImagePicked,
    )
}
