package com.example.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.ask.AskScreen
import com.example.ui.components.TopHeaderBar
import com.example.ui.components.TopTabSegment
import com.example.ui.detail.BlueprintDetailScreen
import com.example.ui.newprogress.DraftBlueprintSheet
import com.example.ui.newprogress.NewProgressScreen
import com.example.viewmodel.AppTab
import com.example.viewmodel.ProgressBankViewModel
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    viewModel: ProgressBankViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    // Auto-clear toast after 2.5 seconds
    LaunchedEffect(uiState.toastMessage) {
        if (uiState.toastMessage != null) {
            delay(2500)
            viewModel.clearToast()
        }
    }

    // Root layout: Box with background per house rules
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .safeDrawingPadding()
    ) {
        // If a blueprint is selected, show detail view
        val selectedBp = uiState.selectedBlueprintId?.let { id ->
            uiState.blueprints.firstOrNull { it.id == id }
        }

        if (selectedBp != null) {
            BlueprintDetailScreen(
                blueprint = selectedBp,
                onBack = { viewModel.navigateBack() },
                onCopyToast = { textToCopy ->
                    clipboardManager.setText(AnnotatedString(textToCopy))
                    viewModel.showToast("Copied to clipboard")
                }
            )
        } else {
            // Main Top Tabs Navigation Interface
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header Bar with App Identity & Notifications
                TopHeaderBar(
                    subtitle = if (uiState.selectedTab == AppTab.ASK) {
                        stringResource(R.string.tab_ask)
                    } else {
                        stringResource(R.string.tab_new_progress)
                    },
                    onNotificationsClick = {
                        viewModel.showToast("No new vault notifications")
                    }
                )

                // Segmented Top Tab Bar
                TopTabSegment(
                    selectedTab = uiState.selectedTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                // Tab Content View
                Crossfade(
                    targetState = uiState.selectedTab,
                    label = "tabCrossfade",
                    modifier = Modifier.weight(1f)
                ) { currentTab ->
                    when (currentTab) {
                        AppTab.ASK -> {
                            AskScreen(
                                uiState = uiState,
                                onTestModeChange = { viewModel.setTestMode(it) },
                                onQueryInputChange = { viewModel.updateAskInput(it) },
                                onSubmitQuery = { viewModel.submitAskQuery(it) },
                                onOpenBlueprint = { viewModel.openBlueprint(it) },
                                onAddProgressClick = { viewModel.openDraftSheet() },
                                onCopyPrompt = { text ->
                                    clipboardManager.setText(AnnotatedString(text))
                                    viewModel.showToast("Copied as agent prompt")
                                }
                            )
                        }

                        AppTab.NEW_PROGRESS -> {
                            NewProgressScreen(
                                uiState = uiState,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                onFilterChange = { viewModel.setCategoryFilter(it) },
                                onOpenBlueprint = { viewModel.openBlueprint(it) },
                                onDraftSpecClick = { viewModel.openDraftSheet() }
                            )
                        }
                    }
                }
            }
        }

        // Quick Draft Bottom Sheet
        DraftBlueprintSheet(
            isOpen = uiState.isDraftSheetOpen,
            onDismiss = { viewModel.closeDraftSheet() },
            onCommit = { title, project, domain ->
                viewModel.commitDraft(title, project, domain)
            }
        )

        // Floating Toast Notification Pill
        AnimatedVisibility(
            visible = uiState.toastMessage != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            uiState.toastMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp))
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                        .testTag("toast_notification_pill"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
