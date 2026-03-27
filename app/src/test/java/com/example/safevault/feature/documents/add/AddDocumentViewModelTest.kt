package com.example.safevault.feature.documents.add

import com.example.safevault.data.repository.InMemoryDocumentsRepository
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddDocumentViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun onSaveClick_blankTitle_setsErrorAndDoesNotSave() = runTest {
        val repository = InMemoryDocumentsRepository()
        val viewModel = AddDocumentViewModel(documentsRepository = repository)

        viewModel.onSaveClick()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.titleError)
        assertTrue(repository.documents.first().isEmpty())
    }

    @Test
    fun onSaveClick_validDocument_emitsSavedAndPersistsDocument() = runTest {
        val repository = InMemoryDocumentsRepository()
        val viewModel = AddDocumentViewModel(documentsRepository = repository)

        viewModel.onTitleChange("  Passeport  ")
        viewModel.onCategoryChange(DocumentCategory.IDENTITY)
        viewModel.onNoteChange("  Renouveler avant juillet  ")
        viewModel.onImagePicked("content://media/external/images/media/42")

        val eventDeferred = async(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.first()
        }

        viewModel.onSaveClick()
        advanceUntilIdle()

        assertEquals(AddDocumentEvent.Saved, eventDeferred.await())
        val savedDocuments = repository.documents.first()
        assertEquals(1, savedDocuments.size)
        assertEquals("Passeport", savedDocuments.first().title)
        assertEquals("Renouveler avant juillet", savedDocuments.first().note)
        assertEquals("content://media/external/images/media/42", savedDocuments.first().imageUri)
    }
}
