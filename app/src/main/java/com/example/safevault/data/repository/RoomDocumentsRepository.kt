package com.example.safevault.data.repository

import com.example.safevault.data.local.dao.DocumentsDao
import com.example.safevault.data.local.mapper.toDomain
import com.example.safevault.data.local.mapper.toEntity
import com.example.safevault.domain.model.VaultDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDocumentsRepository(
    private val documentsDao: DocumentsDao,
) : DocumentsRepository {
    override val documents: Flow<List<VaultDocument>> = documentsDao.observeAll()
        .map { entities -> entities.map { it.toDomain() } }

    override fun observeDocumentById(id: Long): Flow<VaultDocument?> {
        return documentsDao.observeById(id).map { entity -> entity?.toDomain() }
    }

    override suspend fun getDocumentById(id: Long): VaultDocument? {
        return documentsDao.getById(id)?.toDomain()
    }

    override suspend fun upsertDocument(document: VaultDocument): Long {
        val existing = if (document.id == 0L) null else documentsDao.getById(document.id)
        return documentsDao.upsert(
            document = document.toEntity(existingCreatedAtMillis = existing?.createdAtMillis),
        )
    }

    override suspend fun deleteDocumentById(id: Long) {
        documentsDao.deleteById(id)
    }
}
