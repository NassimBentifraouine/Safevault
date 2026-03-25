package com.example.safevault.feature.documents.add

import com.example.safevault.domain.model.DocumentCategory

data class AddDocumentUiState(
    val title: String = "",
    val category: DocumentCategory = DocumentCategory.IDENTITY,
    val expirationTimestampMillis: Long? = null,
    val note: String = "",
    val imageUriInput: String = "",
    val isSaving: Boolean = false,
    val titleError: Boolean = false,
)
