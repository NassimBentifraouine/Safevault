package com.example.safevault.feature.documents.edit

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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditDocumentViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun init_withExistingDocument_populatesForm() = runTest {
        val repository = InMemoryDocumentsRepository()
        val documentId = repository.upsertDocument(
            VaultDocument(
                title = "Attestation mutuelle",
                category = DocumentCategory.INSURANCE,
                note = "Ancienne note",
                imageUri = "content://media/external/images/media/7",
            ),
        )

        val viewModel = EditDocumentViewModel(
            documentId = documentId,
            documentsRepository = repository,
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Attestation mutuelle", viewModel.uiState.value.title)
        assertEquals(DocumentCategory.INSURANCE, viewModel.uiState.value.category)
        assertEquals("content://media/external/images/media/7", viewModel.uiState.value.imageUri)
    }

    @Test
    fun onSaveClick_validEdit_emitsUpdatedAndPersistsChanges() = runTest {
        val repository = InMemoryDocumentsRepository()
        val documentId = repository.upsertDocument(
            VaultDocument(
                title = "Permis",
                category = DocumentCategory.IDENTITY,
                note = "",
                imageUri = null,
            ),
        )
        val viewModel = EditDocumentViewModel(
            documentId = documentId,
            documentsRepository = repository,
        )
        advanceUntilIdle()

        viewModel.onTitleChange("  Permis international  ")
        viewModel.onNoteChange("  A scanner recto verso  ")
        viewModel.onImagePicked("content://media/external/images/media/88")
        val eventDeferred = async(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.first()
        }

        viewModel.onSaveClick()
        advanceUntilIdle()

        assertEquals(EditDocumentEvent.Updated, eventDeferred.await())
        val updated = repository.getDocumentById(documentId)
        assertEquals("Permis international", updated?.title)
        assertEquals("A scanner recto verso", updated?.note)
        assertEquals("content://media/external/images/media/88", updated?.imageUri)
    }

    @Test
    fun init_withMissingDocument_setsNotFound() = runTest {
        val repository = InMemoryDocumentsRepository()
        val viewModel = EditDocumentViewModel(
            documentId = 999L,
            documentsRepository = repository,
        )
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isNotFound)
        assertFalse(viewModel.uiState.value.isLoading)
    }
}
