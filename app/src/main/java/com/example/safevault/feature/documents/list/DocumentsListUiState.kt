package com.example.safevault.feature.documents.list

import com.example.safevault.domain.model.VaultDocument

data class DocumentsListUiState(
    val documents: List<VaultDocument> = emptyList(),
)
