package com.example.safevault.data.repository

import com.example.safevault.domain.model.VaultDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class InMemoryDocumentsRepository : DocumentsRepository {
    private val inMemoryDocuments = MutableStateFlow<List<VaultDocument>>(emptyList())

    override val documents: Flow<List<VaultDocument>> = inMemoryDocuments.asStateFlow()

    override fun observeDocumentById(id: Long): Flow<VaultDocument?> {
        return inMemoryDocuments.asStateFlow().map { docs ->
            docs.firstOrNull { document -> document.id == id }
        }
    }

    override suspend fun getDocumentById(id: Long): VaultDocument? {
        return inMemoryDocuments.value.firstOrNull { document -> document.id == id }
    }

    override suspend fun upsertDocument(document: VaultDocument): Long {
        val current = inMemoryDocuments.value.toMutableList()
        val index = current.indexOfFirst { existing -> existing.id == document.id && document.id != 0L }

        return if (index >= 0) {
            current[index] = document
            inMemoryDocuments.value = current
            document.id
        } else {
            val nextId = (current.maxOfOrNull { existing -> existing.id } ?: 0L) + 1L
            current.add(document.copy(id = nextId))
            inMemoryDocuments.value = current
            nextId
        }
    }

    override suspend fun deleteDocumentById(id: Long) {
        inMemoryDocuments.value = inMemoryDocuments.value.filterNot { document -> document.id == id }
    }
}
