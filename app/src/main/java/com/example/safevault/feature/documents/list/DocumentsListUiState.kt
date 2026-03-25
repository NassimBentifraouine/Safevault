package com.example.safevault.feature.documents.list

import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument

data class DocumentsListUiState(
    val totalDocuments: Int = 0,
    val documents: List<VaultDocument> = emptyList(),
    val selectedCategory: DocumentCategory? = null,
) {
    val hasActiveFilters: Boolean
        get() = selectedCategory != null
}
