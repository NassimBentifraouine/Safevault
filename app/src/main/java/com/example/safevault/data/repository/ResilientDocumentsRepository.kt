package com.example.safevault.data.repository

import com.example.safevault.domain.model.VaultDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ResilientDocumentsRepository(
    private val primary: DocumentsRepository,
    private val fallback: DocumentsRepository,
) : DocumentsRepository {
    @Volatile
    private var fallbackMode: Boolean = false

    override val documents: Flow<List<VaultDocument>> = flow {
        emitAll(activeRepository().documents)
    }.catch {
        fallbackMode = true
        emitAll(fallback.documents)
    }

    override suspend fun getDocumentById(id: Long): VaultDocument? {
        return runCatching {
            activeRepository().getDocumentById(id)
        }.getOrElse {
            fallbackMode = true
            fallback.getDocumentById(id)
        }
    }

    override suspend fun upsertDocument(document: VaultDocument): Long {
        return runCatching {
            activeRepository().upsertDocument(document)
        }.getOrElse {
            fallbackMode = true
            fallback.upsertDocument(document)
        }
    }

    override suspend fun deleteDocumentById(id: Long) {
        runCatching {
            activeRepository().deleteDocumentById(id)
        }.getOrElse {
            fallbackMode = true
            fallback.deleteDocumentById(id)
        }
    }

    private fun activeRepository(): DocumentsRepository {
        return if (fallbackMode) fallback else primary
    }
}
