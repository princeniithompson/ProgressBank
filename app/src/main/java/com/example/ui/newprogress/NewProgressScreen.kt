package com.example.ui.newprogress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.Blueprint
import com.example.ui.components.BlueprintCardItem
import com.example.ui.theme.AppGradients
import com.example.viewmodel.ProgressBankUiState

@Composable
fun NewProgressScreen(
    uiState: ProgressBankUiState,
    onSearchChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onOpenBlueprint: (String) -> Unit,
    onDraftSpecClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter list by category and search query
    val filteredList = uiState.blueprints.filter { bp ->
        val matchesCategory = when (uiState.selectedCategoryFilter) {
            "audio" -> bp.domain.contains("audio") || bp.tags.any { it.contains("audio", ignoreCase = true) }
            "sandbox" -> bp.domain.contains("sandbox") || bp.tags.any { it.contains("webkit", ignoreCase = true) || it.contains("csp", ignoreCase = true) }
            "graphics" -> bp.domain.contains("graphics") || bp.tags.any { it.contains("haptic", ignoreCase = true) || it.contains("graphics", ignoreCase = true) }
            "sync" -> bp.domain.contains("sync") || bp.tags.any { it.contains("crdt", ignoreCase = true) || it.contains("sync", ignoreCase = true) }
            else -> true
        }

        val matchesSearch = uiState.searchQuery.isBlank() ||
                bp.title.contains(uiState.searchQuery, ignoreCase = true) ||
                bp.project.contains(uiState.searchQuery, ignoreCase = true) ||
                bp.tags.any { it.contains(uiState.searchQuery, ignoreCase = true) }

        matchesCategory && matchesSearch
    }

    // Screen root is Box per house layout rules
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Vault Index Header & New Spec Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.vault_index).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.saved_blueprints),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // "+ New Spec" action button with Blended Indigo Gradient
                    Box(
                        modifier = Modifier
                            .background(
                                brush = AppGradients.PrimaryIndigoGradient,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onDraftSpecClick() }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .testTag("new_spec_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.new_spec),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_blueprints_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchChange("") },
                                modifier = Modifier.testTag("search_clear_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.clear_search),
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_blueprints_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedBorderColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    singleLine = true
                )
            }

            // Category Filter Chips
            item {
                val filters = listOf(
                    "all" to "All Specs (${uiState.blueprints.size})",
                    "audio" to "Audio",
                    "sandbox" to "Web & Sandbox",
                    "graphics" to "Hardware & UI",
                    "sync" to "Data & CRDT"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(filters) { (key, label) ->
                        val isSelected = uiState.selectedCategoryFilter == key
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { onFilterChange(key) }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                                .testTag("filter_chip_$key")
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Blueprint Cards
            if (filteredList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matching blueprints",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your search terms or draft a new technical schema.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(filteredList) { bp ->
                    BlueprintCardItem(
                        blueprint = bp,
                        onClick = { onOpenBlueprint(bp.id) }
                    )
                }
            }

            // Extra padding at list bottom so floating button doesn't obscure content
            item {
                Spacer(modifier = Modifier.height(88.dp))
            }
        }

        // Floating Action Button: "Draft Spec" with Blended Indigo Gradient
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(20.dp)
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp))
                .background(
                    brush = AppGradients.PrimaryIndigoGradient,
                    shape = RoundedCornerShape(28.dp)
                )
                .clickable { onDraftSpecClick() }
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .testTag("draft_spec_fab")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Architecture,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.draft_spec),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
