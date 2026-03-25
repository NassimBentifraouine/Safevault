package com.example.safevault.feature.documents.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.safevault.data.repository.DocumentsRepository
import com.example.safevault.domain.model.DocumentCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DocumentsListViewModel(
    documentsRepository: DocumentsRepository,
) : ViewModel() {
    private val selectedCategory = MutableStateFlow<DocumentCategory?>(null)

    private val documentsStream = documentsRepository.documents
        .catch { emit(emptyList()) }

    val uiState: StateFlow<DocumentsListUiState> = combine(
        documentsStream,
        selectedCategory,
    ) { documents, category ->
        val filtered = documents.filter { document ->
            category == null || document.category == category
        }

        DocumentsListUiState(
            totalDocuments = documents.size,
            documents = filtered,
            selectedCategory = category,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = DocumentsListUiState(),
        )

    fun onCategoryFilterChange(category: DocumentCategory?) {
        selectedCategory.value = category
    }

    fun onResetFiltersClick() {
        selectedCategory.value = null
    }

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
