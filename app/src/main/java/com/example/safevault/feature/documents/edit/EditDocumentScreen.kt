package com.example.safevault.feature.documents.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.safevault.R
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.feature.documents.common.DocumentCategorySelector
import com.example.safevault.feature.documents.common.DocumentFormHeader
import com.example.safevault.feature.documents.common.DocumentFormSectionCard
import com.example.safevault.feature.documents.common.DocumentImageField
import com.example.safevault.ui.theme.SafeVaultTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDocumentScreen(
    uiState: EditDocumentUiState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (DocumentCategory) -> Unit,
    onNoteChange: (String) -> Unit,
    onImagePicked: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        onImagePicked(uri?.toString())
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.document_edit_title)) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = stringResource(id = R.string.action_back),
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                )
            },
            bottomBar = {
                if (!uiState.isNotFound) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                        ) {
                            Button(
                                onClick = onSaveClick,
                                enabled = !uiState.isSaving && !uiState.isLoading,
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 14.dp),
                                shape = MaterialTheme.shapes.large,
                            ) {
                                if (uiState.isSaving) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.Save,
                                        contentDescription = null,
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 8.dp),
                                        text = stringResource(id = R.string.document_edit_save),
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                }
                            }
                        }
                    }
                }
            },
        ) { innerPadding ->
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.isNotFound -> {
                    EditNotFoundCard(
                        onBackClick = onBackClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(20.dp),
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        DocumentFormHeader(
                            title = stringResource(id = R.string.document_edit_title),
                            subtitle = stringResource(id = R.string.document_edit_subtitle),
                        )

                        DocumentFormSectionCard(title = stringResource(id = R.string.document_add_section_main)) {
                            OutlinedTextField(
                                value = uiState.title,
                                onValueChange = onTitleChange,
                                label = { Text(text = stringResource(id = R.string.document_add_label_title)) },
                                placeholder = { Text(text = stringResource(id = R.string.document_add_placeholder_title)) },
                                supportingText = {
                                    if (uiState.titleError) {
                                        Text(text = stringResource(id = R.string.document_add_error_title_required))
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                ),
                                isError = uiState.titleError,
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Text(
                                text = stringResource(id = R.string.document_add_label_category),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            DocumentCategorySelector(
                                selected = uiState.category,
                                onCategoryChange = onCategoryChange,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        DocumentFormSectionCard(title = stringResource(id = R.string.document_add_section_note)) {
                            OutlinedTextField(
                                value = uiState.note,
                                onValueChange = onNoteChange,
                                placeholder = { Text(text = stringResource(id = R.string.document_add_placeholder_note)) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 4,
                                maxLines = 6,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                ),
                            )

                            DocumentImageField(
                                imageUri = uiState.imageUri,
                                onPickImage = {
                                    pickImageLauncher.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                                        ),
                                    )
                                },
                                onRemoveImage = { onImagePicked(null) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditNotFoundCard(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.document_edit_not_found_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(id = R.string.document_edit_not_found_body),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = onBackClick) {
                Text(text = stringResource(id = R.string.action_back))
            }
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
private fun EditDocumentPreview() {
    SafeVaultTheme {
        EditDocumentScreen(
            uiState = EditDocumentUiState(
                isLoading = false,
                title = "Passeport",
                note = "Verifier renouvellement au moins 6 mois avant voyage.",
            ),
            onBackClick = {},
            onSaveClick = {},
            onTitleChange = {},
            onCategoryChange = {},
            onNoteChange = {},
            onImagePicked = {},
        )
    }
}
