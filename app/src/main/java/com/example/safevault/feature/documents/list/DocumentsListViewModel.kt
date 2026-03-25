package com.example.safevault.feature.documents.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.safevault.data.repository.DocumentsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DocumentsListViewModel(
    documentsRepository: DocumentsRepository,
) : ViewModel() {
    val uiState: StateFlow<DocumentsListUiState> = documentsRepository.documents
        .catch { emit(emptyList()) }
        .map { documents ->
            DocumentsListUiState(documents = documents)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = DocumentsListUiState(),
        )

    companion object {
        fun provideFactory(
            documentsRepository: DocumentsRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DocumentsListViewModel(documentsRepository = documentsRepository)
            }
        }
    }
}
