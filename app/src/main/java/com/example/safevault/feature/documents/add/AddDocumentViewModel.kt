package com.example.safevault.feature.documents.add

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

class AddDocumentViewModel(
    private val documentsRepository: DocumentsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddDocumentUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddDocumentEvent>()
    val events: SharedFlow<AddDocumentEvent> = _events.asSharedFlow()

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

    fun onImageUriInputChange(value: String) {
        _uiState.update { state -> state.copy(imageUriInput = value) }
    }

    fun onSaveClick() {
        val currentState = _uiState.value
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
                        title = normalizedTitle,
                        category = currentState.category,
                        expirationTimestampMillis = currentState.expirationTimestampMillis,
                        note = currentState.note.trim(),
                        imageUri = currentState.imageUriInput.trim().ifBlank { null },
                    ),
                )
                _events.emit(AddDocumentEvent.Saved)
            } finally {
                _uiState.update { state -> state.copy(isSaving = false) }
            }
        }
    }

    companion object {
        fun provideFactory(
            documentsRepository: DocumentsRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AddDocumentViewModel(documentsRepository = documentsRepository)
            }
        }
    }
}

sealed interface AddDocumentEvent {
    data object Saved : AddDocumentEvent
}
