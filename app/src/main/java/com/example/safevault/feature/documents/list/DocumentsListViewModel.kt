package com.example.safevault.feature.documents.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.safevault.data.repository.DocumentsRepository
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

private const val ONE_DAY_MILLIS = 86_400_000L
private const val WARNING_THRESHOLD_DAYS = 30L

class DocumentsListViewModel(
    documentsRepository: DocumentsRepository,
) : ViewModel() {
    private val selectedCategory = MutableStateFlow<DocumentCategory?>(null)
    private val selectedExpirationFilter = MutableStateFlow(DocumentsExpirationFilter.ALL)
    private val selectedSortOption = MutableStateFlow(DocumentsSortOption.EXPIRATION_ASC)

    private val documentsStream = documentsRepository.documents
        .catch { emit(emptyList()) }

    val uiState: StateFlow<DocumentsListUiState> = combine(
        documentsStream,
        selectedCategory,
        selectedExpirationFilter,
        selectedSortOption,
    ) { documents, category, expirationFilter, sortOption ->
        val filtered = documents
            .asSequence()
            .filter { document -> matchesCategory(document = document, selectedCategory = category) }
            .filter { document ->
                matchesExpirationFilter(
                    document = document,
                    selectedFilter = expirationFilter,
                )
            }
            .sortedWith(sortComparator(sortOption = sortOption))
            .toList()

        DocumentsListUiState(
            totalDocuments = documents.size,
            documents = filtered,
            selectedCategory = category,
            selectedExpirationFilter = expirationFilter,
            selectedSortOption = sortOption,
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

    fun onExpirationFilterChange(filter: DocumentsExpirationFilter) {
        selectedExpirationFilter.value = filter
    }

    fun onSortOptionChange(option: DocumentsSortOption) {
        selectedSortOption.value = option
    }

    fun onResetFiltersClick() {
        selectedCategory.value = null
        selectedExpirationFilter.value = DocumentsExpirationFilter.ALL
    }

    private fun matchesCategory(
        document: VaultDocument,
        selectedCategory: DocumentCategory?,
    ): Boolean {
        return selectedCategory == null || document.category == selectedCategory
    }

    private fun matchesExpirationFilter(
        document: VaultDocument,
        selectedFilter: DocumentsExpirationFilter,
    ): Boolean {
        val expiration = document.expirationTimestampMillis
        val nowMillis = System.currentTimeMillis()
        val warningThresholdMillis = nowMillis + (WARNING_THRESHOLD_DAYS * ONE_DAY_MILLIS)

        return when (selectedFilter) {
            DocumentsExpirationFilter.ALL -> true
            DocumentsExpirationFilter.NO_DATE -> expiration == null
            DocumentsExpirationFilter.EXPIRED -> expiration != null && expiration < nowMillis
            DocumentsExpirationFilter.EXPIRING_SOON -> {
                expiration != null &&
                    expiration >= nowMillis &&
                    expiration <= warningThresholdMillis
            }
        }
    }

    private fun sortComparator(sortOption: DocumentsSortOption): Comparator<VaultDocument> {
        return when (sortOption) {
            DocumentsSortOption.EXPIRATION_ASC -> compareBy<VaultDocument>(
                { document -> document.expirationTimestampMillis == null },
                { document -> document.expirationTimestampMillis ?: Long.MAX_VALUE },
                { document -> document.title.lowercase(Locale.getDefault()) },
            )

            DocumentsSortOption.TITLE_ASC -> compareBy<VaultDocument>(
                { document -> document.title.lowercase(Locale.getDefault()) },
                { document -> document.expirationTimestampMillis ?: Long.MAX_VALUE },
            )
        }
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
