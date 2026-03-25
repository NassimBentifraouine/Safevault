package com.example.safevault.feature.documents.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.safevault.data.repository.DocumentsRepository
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditDocumentViewModel(
    private val documentId: Long,
    private val documentsRepository: DocumentsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditDocumentUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EditDocumentEvent>()
    val events: SharedFlow<EditDocumentEvent> = _events.asSharedFlow()

    init {
        loadDocument()
    }

    fun onTitleChange(value: String) {
        _uiState.update { state ->
            state.copy(
                title = value,
                titleError = false,
            )
        }
    }

    fun onCategoryChange(value: DocumentCategory) {
        _uiState.update { state -> state.copy(category = value) }
    }

    fun onExpirationDateChange(value: Long?) {
        _uiState.update { state -> state.copy(expirationTimestampMillis = value) }
    }

    fun onNoteChange(value: String) {
        _uiState.update { state -> state.copy(note = value) }
    }

    fun onImagePicked(uri: String?) {
        _uiState.update { state -> state.copy(imageUri = uri) }
    }

    fun onSaveClick() {
        val currentState = _uiState.value
        if (currentState.isLoading || currentState.isSaving || currentState.isNotFound) {
            return
        }

        val normalizedTitle = currentState.title.trim()
        if (normalizedTitle.isBlank()) {
            _uiState.update { state -> state.copy(titleError = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { state -> state.copy(isSaving = true) }
            try {
                documentsRepository.upsertDocument(
                    document = VaultDocument(
                        id = documentId,
                        title = normalizedTitle,
                        category = currentState.category,
                        expirationTimestampMillis = currentState.expirationTimestampMillis,
                        note = currentState.note.trim(),
                        imageUri = currentState.imageUri,
                    ),
                )
                _events.emit(EditDocumentEvent.Updated)
            } catch (_: Throwable) {
                _events.emit(EditDocumentEvent.UpdateFailed)
            } finally {
                _uiState.update { state -> state.copy(isSaving = false) }
            }
        }
    }

    private fun loadDocument() {
        viewModelScope.launch {
            val document = runCatching {
                documentsRepository.getDocumentById(documentId)
            }.getOrNull()

            if (document == null) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isNotFound = true,
                    )
                }
                return@launch
            }

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    title = document.title,
                    category = document.category,
                    expirationTimestampMillis = document.expirationTimestampMillis,
                    note = document.note,
                    imageUri = document.imageUri,
                )
            }
        }
    }

    companion object {
        fun provideFactory(
            documentId: Long,
            documentsRepository: DocumentsRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                EditDocumentViewModel(
                    documentId = documentId,
                    documentsRepository = documentsRepository,
                )
            }
        }
    }
}

sealed interface EditDocumentEvent {
    data object Updated : EditDocumentEvent
    data object UpdateFailed : EditDocumentEvent
}
