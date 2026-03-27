package com.example.safevault.feature.documents.detail

import com.example.safevault.data.repository.InMemoryDocumentsRepository
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument
import com.example.safevault.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DocumentDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun init_withExistingDocument_updatesUiState() = runTest {
        val repository = InMemoryDocumentsRepository()
        val documentId = repository.upsertDocument(
            VaultDocument(
                title = "Carte vitale",
                category = DocumentCategory.HEALTH,
                note = "",
                imageUri = null,
            ),
        )

        val viewModel = DocumentDetailViewModel(
            documentId = documentId,
            documentsRepository = repository,
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(documentId, viewModel.uiState.value.document?.id)
    }

    @Test
    fun onDeleteConfirm_existingDocument_emitsDeletedAndRemovesDocument() = runTest {
        val repository = InMemoryDocumentsRepository()
        val documentId = repository.upsertDocument(
            VaultDocument(
                title = "Contrat assurance",
                category = DocumentCategory.INSURANCE,
                note = "",
                imageUri = null,
            ),
        )

        val viewModel = DocumentDetailViewModel(
            documentId = documentId,
            documentsRepository = repository,
        )
        advanceUntilIdle()
        val eventDeferred = async(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.first()
        }

        viewModel.onDeleteConfirm()
        advanceUntilIdle()

        assertEquals(DocumentDetailEvent.Deleted, eventDeferred.await())
        assertNull(repository.getDocumentById(documentId))
    }
}
