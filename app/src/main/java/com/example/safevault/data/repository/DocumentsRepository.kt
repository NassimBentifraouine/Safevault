package com.example.safevault.data.repository

import com.example.safevault.domain.model.VaultDocument
import kotlinx.coroutines.flow.Flow

interface DocumentsRepository {
    val documents: Flow<List<VaultDocument>>

    suspend fun getDocumentById(id: Long): VaultDocument?

    suspend fun upsertDocument(document: VaultDocument): Long

    suspend fun deleteDocumentById(id: Long)
}
