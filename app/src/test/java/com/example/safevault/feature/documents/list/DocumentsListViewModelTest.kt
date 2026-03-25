package com.example.safevault.feature.documents.list

import com.example.safevault.data.repository.InMemoryDocumentsRepository
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument
import com.example.safevault.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DocumentsListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun categoryFilter_filtersAndResetsDocuments() = runTest {
        val repository = InMemoryDocumentsRepository()
        repository.upsertDocument(
            VaultDocument(
                title = "Carte identite",
                category = DocumentCategory.IDENTITY,
                expirationTimestampMillis = null,
                note = "",
                imageUri = null,
            ),
        )
        repository.upsertDocument(
            VaultDocument(
                title = "Carnet vaccination",
                category = DocumentCategory.HEALTH,
                expirationTimestampMillis = null,
                note = "",
                imageUri = null,
            ),
        )
        repository.upsertDocument(
            VaultDocument(
                title = "Mutuelle",
                category = DocumentCategory.HEALTH,
                expirationTimestampMillis = null,
                note = "",
                imageUri = null,
            ),
        )
        val viewModel = DocumentsListViewModel(documentsRepository = repository)
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()
        assertEquals(3, viewModel.uiState.value.totalDocuments)
        assertEquals(3, viewModel.uiState.value.documents.size)

        viewModel.onCategoryFilterChange(DocumentCategory.HEALTH)
        advanceUntilIdle()

        val filtered = viewModel.uiState.value.documents
        assertEquals(2, filtered.size)
        assertTrue(filtered.all { document -> document.category == DocumentCategory.HEALTH })
        assertTrue(viewModel.uiState.value.hasActiveFilters)

        viewModel.onResetFiltersClick()
        advanceUntilIdle()

        assertEquals(3, viewModel.uiState.value.documents.size)
        assertFalse(viewModel.uiState.value.hasActiveFilters)
        collectJob.cancel()
    }
}
