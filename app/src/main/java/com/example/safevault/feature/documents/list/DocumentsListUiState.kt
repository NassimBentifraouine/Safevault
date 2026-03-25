package com.example.safevault.feature.documents.list

import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument

data class DocumentsListUiState(
    val totalDocuments: Int = 0,
    val documents: List<VaultDocument> = emptyList(),
    val selectedCategory: DocumentCategory? = null,
    val selectedExpirationFilter: DocumentsExpirationFilter = DocumentsExpirationFilter.ALL,
    val selectedSortOption: DocumentsSortOption = DocumentsSortOption.EXPIRATION_ASC,
) {
    val hasActiveFilters: Boolean
        get() = selectedCategory != null || selectedExpirationFilter != DocumentsExpirationFilter.ALL
}

enum class DocumentsExpirationFilter {
    ALL,
    EXPIRING_SOON,
    EXPIRED,
    NO_DATE,
}

enum class DocumentsSortOption {
    EXPIRATION_ASC,
    TITLE_ASC,
}
