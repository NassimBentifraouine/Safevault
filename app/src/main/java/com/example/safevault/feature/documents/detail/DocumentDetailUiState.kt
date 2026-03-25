package com.example.safevault.feature.documents.detail

import com.example.safevault.domain.model.VaultDocument

data class DocumentDetailUiState(
    val isLoading: Boolean = true,
    val isDeleting: Boolean = false,
    val document: VaultDocument? = null,
)
