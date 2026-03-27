package com.example.safevault.data.local.mapper

import com.example.safevault.data.local.entity.DocumentEntity
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument
import java.time.Instant

fun DocumentEntity.toDomain(): VaultDocument {
    return VaultDocument(
        id = id,
        title = title,
        category = category.toDocumentCategory(),
        note = note,
        imageUri = imageUri,
    )
}

fun VaultDocument.toEntity(existingCreatedAtMillis: Long? = null): DocumentEntity {
    return DocumentEntity(
        id = id,
        title = title,
        category = category.name,
        note = note,
        imageUri = imageUri,
        createdAtMillis = existingCreatedAtMillis ?: Instant.now().toEpochMilli(),
    )
}

private fun String.toDocumentCategory(): DocumentCategory {
    return DocumentCategory.entries.firstOrNull { it.name == this }
        ?: DocumentCategory.OTHER
}
