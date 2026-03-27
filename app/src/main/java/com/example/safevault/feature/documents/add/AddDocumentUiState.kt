package com.example.safevault.feature.documents.add

import com.example.safevault.domain.model.DocumentCategory

data class AddDocumentUiState(
    val title: String = "",
    val category: DocumentCategory = DocumentCategory.IDENTITY,
    val note: String = "",
    val imageUri: String? = null,
    val isSaving: Boolean = false,
    val titleError: Boolean = false,
)
