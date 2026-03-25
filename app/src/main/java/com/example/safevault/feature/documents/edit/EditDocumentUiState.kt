package com.example.safevault.feature.documents.edit

import com.example.safevault.domain.model.DocumentCategory

data class EditDocumentUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isNotFound: Boolean = false,
    val title: String = "",
    val category: DocumentCategory = DocumentCategory.IDENTITY,
    val expirationTimestampMillis: Long? = null,
    val note: String = "",
    val imageUri: String? = null,
    val titleError: Boolean = false,
)
