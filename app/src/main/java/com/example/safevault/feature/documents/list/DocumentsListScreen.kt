package com.example.safevault.feature.documents.list

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safevault.R
import com.example.safevault.domain.model.DocumentCategory
import com.example.safevault.domain.model.VaultDocument
import com.example.safevault.ui.theme.SafeVaultTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.floor

private const val ONE_DAY_MILLIS = 86_400_000L
private const val WARNING_THRESHOLD_DAYS = 30L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsListScreen(
    uiState: DocumentsListUiState,
    onAddDocumentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    ),
                ),
            ),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.documents_screen_title),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onAddDocumentClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                        )
                    },
                    text = {
                        Text(text = stringResource(id = R.string.documents_add_short))
                    },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = innerPadding.calculateBottomPadding() + 96.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    VaultOverviewCard(totalDocuments = uiState.documents.size)
                }

                if (uiState.documents.isEmpty()) {
                    item {
                        EmptyDocumentsCard(onAddDocumentClick = onAddDocumentClick)
                    }
                } else {
                    items(items = uiState.documents, key = { document -> document.id }) { document ->
                        DocumentCard(document = document)
                    }
                }
            }
        }
    }
}

@Composable
private fun VaultOverviewCard(
    totalDocuments: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f),
                            MaterialTheme.colorScheme.surface,
                        ),
                    ),
                )
                .padding(24.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Icon(
                            modifier = Modifier.padding(8.dp),
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.documents_overview_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Text(
                    text = stringResource(id = R.string.documents_overview_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(
                        id = R.string.documents_count,
                        totalDocuments,
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun EmptyDocumentsCard(
    onAddDocumentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    imageVector = Icons.Outlined.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Text(
                text = stringResource(id = R.string.documents_empty_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = R.string.documents_empty_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            ExtendedFloatingActionButton(
                onClick = onAddDocumentClick,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(text = stringResource(id = R.string.documents_empty_action))
                },
            )
        }
    }
}

@Composable
private fun DocumentCard(
    document: VaultDocument,
    modifier: Modifier = Modifier,
) {
    val expirationInfo = rememberExpirationInfo(expirationTimestampMillis = document.expirationTimestampMillis)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = document.title,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = document.category.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                ExpirationBadge(expirationInfo = expirationInfo)
            }

            Text(
                text = expirationLabel(expirationTimestampMillis = document.expirationTimestampMillis),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (document.note.isNotBlank()) {
                Text(
                    text = document.note,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun ExpirationBadge(
    expirationInfo: ExpirationInfo,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = expirationInfo.containerColor,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = expirationInfo.label,
            style = MaterialTheme.typography.labelMedium,
            color = expirationInfo.contentColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun rememberExpirationInfo(
    expirationTimestampMillis: Long?,
): ExpirationInfo {
    val colorScheme = MaterialTheme.colorScheme
    val nowMillis = System.currentTimeMillis()

    if (expirationTimestampMillis == null) {
        return ExpirationInfo(
            label = stringResource(id = R.string.documents_badge_no_date),
            containerColor = colorScheme.surfaceVariant,
            contentColor = colorScheme.onSurfaceVariant,
        )
    }

    val remainingDays = floor(
        (expirationTimestampMillis - nowMillis).toDouble() / ONE_DAY_MILLIS.toDouble(),
    ).toLong()

    return when {
        remainingDays < 0L -> ExpirationInfo(
            label = stringResource(id = R.string.documents_badge_expired),
            containerColor = colorScheme.errorContainer,
            contentColor = colorScheme.onErrorContainer,
        )

        remainingDays == 0L -> ExpirationInfo(
            label = stringResource(id = R.string.documents_badge_today),
            containerColor = colorScheme.errorContainer,
            contentColor = colorScheme.onErrorContainer,
        )

        remainingDays <= WARNING_THRESHOLD_DAYS -> ExpirationInfo(
            label = stringResource(
                id = R.string.documents_badge_warning,
                remainingDays.toInt(),
            ),
            containerColor = colorScheme.tertiaryContainer,
            contentColor = colorScheme.onTertiaryContainer,
        )

        else -> ExpirationInfo(
            label = stringResource(id = R.string.documents_badge_valid),
            containerColor = colorScheme.secondaryContainer,
            contentColor = colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
private fun expirationLabel(expirationTimestampMillis: Long?): String {
    if (expirationTimestampMillis == null) {
        return stringResource(id = R.string.documents_expiration_no_date)
    }

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.FRANCE)
    return stringResource(
        id = R.string.documents_expiration_label,
        formatter.format(Date(expirationTimestampMillis)),
    )
}

private data class ExpirationInfo(
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
)

@Preview(showBackground = true)
@Composable
private fun DocumentsListEmptyPreview() {
    SafeVaultTheme {
        DocumentsListScreen(
            uiState = DocumentsListUiState(),
            onAddDocumentClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DocumentsListFilledPreview() {
    val now = System.currentTimeMillis()
    val sampleDocuments = listOf(
        VaultDocument(
            id = 1L,
            title = "Carte d'identite",
            category = DocumentCategory.IDENTITY,
            expirationTimestampMillis = now + (8 * ONE_DAY_MILLIS),
            note = "Renouvellement a anticiper.",
            imageUri = null,
        ),
        VaultDocument(
            id = 2L,
            title = "Assurance habitation",
            category = DocumentCategory.INSURANCE,
            expirationTimestampMillis = now + (90 * ONE_DAY_MILLIS),
            note = "",
            imageUri = null,
        ),
    )

    SafeVaultTheme {
        DocumentsListScreen(
            uiState = DocumentsListUiState(documents = sampleDocuments),
            onAddDocumentClick = {},
        )
    }
}
