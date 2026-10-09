package com.example.viewmodel

import com.example.data.model.Blueprint

enum class AppTab {
    ASK,
    NEW_PROGRESS
}

enum class TestMode {
    MATCH,
    NO_MATCH
}

data class ProgressBankUiState(
    val selectedTab: AppTab = AppTab.ASK,
    val selectedBlueprintId: String? = null,
    val isDraftSheetOpen: Boolean = false,
    val testMode: TestMode = TestMode.MATCH,
    val askInput: String = "",
    val hasSubmittedAsk: Boolean = true,
    val submittedQuery: String = "How did I fix audio ducking glitch during background speech recognition?",
    val rawAnswerText: String = "Use AVAudioSessionCategoryPlayAndRecord with .duckOthers and avoid re-activating the session on the audio worker thread. Keep activation on the main thread and defer session deactivation with a 150ms timeout.",
    val matchedBlueprint: Blueprint? = null,
    val blueprints: List<Blueprint> = emptyList(),
    val searchQuery: String = "",
    val selectedCategoryFilter: String = "all",
    val toastMessage: String? = null
)
