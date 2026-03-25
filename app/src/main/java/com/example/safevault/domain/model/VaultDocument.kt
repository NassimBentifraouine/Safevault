package com.example.safevault.domain.model

data class VaultDocument(
    val id: Long = 0L,
    val title: String,
    val category: DocumentCategory,
    val expirationTimestampMillis: Long?,
    val note: String,
    val imageUri: String?,
)
