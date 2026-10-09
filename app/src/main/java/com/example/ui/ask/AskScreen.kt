package com.example.ui.ask

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Psychology
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.AppGradients
import com.example.viewmodel.ProgressBankUiState
import com.example.viewmodel.TestMode

@Composable
fun AskScreen(
    uiState: ProgressBankUiState,
    onTestModeChange: (TestMode) -> Unit,
    onQueryInputChange: (String) -> Unit,
    onSubmitQuery: (String?) -> Unit,
    onOpenBlueprint: (String) -> Unit,
    onAddProgressClick: () -> Unit,
    onCopyPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Screen root is Box per house layout rules
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Scrollable Message Content Stream
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 96.dp) // Leave space for pinned bottom composer
        ) {
            // Small Test Control Toggle Bar
            TestModeToggleBar(
                currentMode = uiState.testMode,
                onModeSelected = onTestModeChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )

            // Dynamic Body State
            when {
                !uiState.hasSubmittedAsk -> {
                    AskEmptyHero(
                        onSuggestionClick = { suggestion ->
                            onSubmitQuery(suggestion)
                        }
                    )
                }

                uiState.testMode == TestMode.MATCH -> {
                    AskMatchContent(
                        queryText = uiState.submittedQuery,
                        rawAnswerText = uiState.rawAnswerText,
                        blueprint = uiState.matchedBlueprint,
                        onOpenBlueprint = onOpenBlueprint,
                        onCopyPrompt = onCopyPrompt
                    )
                }

                else -> {
                    AskNoMatchContent(
                        queryText = uiState.submittedQuery,
                        existingBlueprints = uiState.blueprints,
                        onAddProgressClick = onAddProgressClick,
                        onBlueprintClick = onOpenBlueprint
                    )
                }
            }
        }

        // Pinned Bottom Composer Dock
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            AskComposer(
                inputText = uiState.askInput,
                onInputChange = onQueryInputChange,
                onSend = { onSubmitQuery(null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Test Control Toggle: Match / No match.
 */
@Composable
private fun TestModeToggleBar(
    currentMode: TestMode,
    onModeSelected: (TestMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Preview state:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(2.dp)
        ) {
            // Match pill
            Box(
                modifier = Modifier
                    .background(
                        color = if (currentMode == TestMode.MATCH) {
                            MaterialTheme.colorScheme.surfaceContainerLowest
                        } else {
                            MaterialTheme.colorScheme.surfaceContainer
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onModeSelected(TestMode.MATCH) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("test_mode_match_button")
            ) {
                Text(
                    text = stringResource(R.string.mode_match),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (currentMode == TestMode.MATCH) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Spacer(modifier = Modifier.width(2.dp))

            // No match pill
            Box(
                modifier = Modifier
                    .background(
                        color = if (currentMode == TestMode.NO_MATCH) {
                            MaterialTheme.colorScheme.surfaceContainerLowest
                        } else {
                            MaterialTheme.colorScheme.surfaceContainer
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onModeSelected(TestMode.NO_MATCH) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("test_mode_no_match_button")
            ) {
                Text(
                    text = stringResource(R.string.mode_no_match),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (currentMode == TestMode.NO_MATCH) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

/**
 * Fixed Bottom Composer with Blended Indigo Gradient send button.
 */
@Composable
private fun AskComposer(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(28.dp))
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.ask_composer_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
                    .testTag("query_input_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() })
            )

            // Send Button with Blended Indigo Gradient
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        brush = AppGradients.PrimaryIndigoGradient,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .clickable { onSend() }
                    .testTag("send_query_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = stringResource(R.string.send_query),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
