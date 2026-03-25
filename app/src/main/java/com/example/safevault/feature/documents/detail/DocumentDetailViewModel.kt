package com.example.safevault.feature.documents.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.safevault.data.repository.DocumentsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DocumentDetailViewModel(
    private val documentId: Long,
    private val documentsRepository: DocumentsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DocumentDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DocumentDetailEvent>()
    val events: SharedFlow<DocumentDetailEvent> = _events.asSharedFlow()

    init {
        refreshDocument()
    }

    fun onDeleteConfirm() {
        val currentDocument = _uiState.value.document ?: return
        if (_uiState.value.isDeleting) return

        viewModelScope.launch {
            _uiState.update { state -> state.copy(isDeleting = true) }
            try {
                documentsRepository.deleteDocumentById(currentDocument.id)
                _events.emit(DocumentDetailEvent.Deleted)
            } catch (_: Throwable) {
                _events.emit(DocumentDetailEvent.DeleteFailed)
            } finally {
                _uiState.update { state -> state.copy(isDeleting = false) }
            }
        }
    }

    private fun refreshDocument() {
        viewModelScope.launch {
            val document = runCatching {
                documentsRepository.getDocumentById(documentId)
            }.getOrNull()
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    document = document,
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
                DocumentDetailViewModel(
                    documentId = documentId,
                    documentsRepository = documentsRepository,
                )
            }
        }
    }
}

sealed interface DocumentDetailEvent {
    data object Deleted : DocumentDetailEvent
    data object DeleteFailed : DocumentDetailEvent
}
