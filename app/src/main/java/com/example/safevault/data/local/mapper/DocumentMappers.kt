package com.example.safevault.data.local.mapper

import com.example.safevault.data.local.entity.DocumentEntity
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument

fun DocumentEntity.toDomain(): VaultDocument {
    return VaultDocument(
        id = id,
        title = title,
        category = category.toDocumentCategory(),
        expirationTimestampMillis = expirationTimestampMillis,
        note = note,
        imageUri = imageUri,
    )
}

fun VaultDocument.toEntity(existingCreatedAtMillis: Long? = null): DocumentEntity {
    return DocumentEntity(
        id = id,
        title = title,
        category = category.name,
        expirationTimestampMillis = expirationTimestampMillis,
        note = note,
        imageUri = imageUri,
        createdAtMillis = existingCreatedAtMillis ?: System.currentTimeMillis(),
    )
}

private fun String.toDocumentCategory(): DocumentCategory {
    return DocumentCategory.entries.firstOrNull { category -> category.name == this }
        ?: DocumentCategory.OTHER
}
