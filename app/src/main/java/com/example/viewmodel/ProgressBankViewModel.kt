package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Blueprint
import com.example.data.repository.BlueprintRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressBankViewModel(
    private val repository: BlueprintRepository = BlueprintRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressBankUiState())
    val uiState: StateFlow<ProgressBankUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.blueprints.collect { list ->
                val match = list.firstOrNull { it.id == "audio-ducking-session" } ?: list.firstOrNull()
                _uiState.update { current ->
                    current.copy(
                        blueprints = list,
                        matchedBlueprint = if (current.testMode == TestMode.MATCH) match else null
                    )
                }
            }
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setTestMode(mode: TestMode) {
        val list = _uiState.value.blueprints
        val match = list.firstOrNull { it.id == "audio-ducking-session" } ?: list.firstOrNull()

        _uiState.update { current ->
            if (mode == TestMode.MATCH) {
                current.copy(
                    testMode = mode,
                    hasSubmittedAsk = true,
                    submittedQuery = "How did I fix audio ducking glitch during background speech recognition?",
                    rawAnswerText = "Use AVAudioSessionCategoryPlayAndRecord with .duckOthers and avoid re-activating the session on the audio worker thread. Keep activation on the main thread and defer session deactivation with a 150ms timeout.",
                    matchedBlueprint = match
                )
            } else {
                current.copy(
                    testMode = mode,
                    hasSubmittedAsk = true,
                    submittedQuery = "How did I configure WebRTC TURN relay over port 443?",
                    rawAnswerText = "",
                    matchedBlueprint = null
                )
            }
        }
    }

    fun updateAskInput(input: String) {
        _uiState.update { it.copy(askInput = input) }
    }

    fun submitAskQuery(queryText: String? = null) {
        val text = (queryText ?: _uiState.value.askInput).trim()
        if (text.isEmpty()) return

        val isMatch = _uiState.value.testMode == TestMode.MATCH
        val match = if (isMatch) {
            _uiState.value.blueprints.firstOrNull { it.id == "audio-ducking-session" }
                ?: _uiState.value.blueprints.firstOrNull()
        } else null

        _uiState.update { current ->
            current.copy(
                hasSubmittedAsk = true,
                submittedQuery = text,
                askInput = "",
                matchedBlueprint = match,
                rawAnswerText = if (isMatch) {
                    "Use AVAudioSessionCategoryPlayAndRecord with .duckOthers and avoid re-activating the session on the audio worker thread. Keep activation on the main thread and defer session deactivation with a 150ms timeout."
                } else ""
            )
        }
    }

    fun openBlueprint(id: String) {
        _uiState.update { it.copy(selectedBlueprintId = id) }
    }

    fun navigateBack(): Boolean {
        if (_uiState.value.selectedBlueprintId != null) {
            _uiState.update { it.copy(selectedBlueprintId = null) }
            return true
        }
        return false
    }

    fun openDraftSheet() {
        _uiState.update { it.copy(isDraftSheetOpen = true) }
    }

    fun closeDraftSheet() {
        _uiState.update { it.copy(isDraftSheetOpen = false) }
    }

    fun commitDraft(title: String, project: String, domain: String) {
        val safeTitle = title.ifBlank { "Untitled Architecture Spec" }
        val safeProject = project.ifBlank { "Workbench" }
        val safeDomain = domain.ifBlank { "General" }

        val newBlueprint = Blueprint(
            id = "spec-${System.currentTimeMillis()}",
            title = safeTitle,
            project = safeProject,
            date = "Just now",
            domain = safeDomain.lowercase(),
            tags = listOf(safeDomain, "New"),
            problemContext = "Documented architectural milestone for $safeTitle in $safeProject.",
            solutionSummary = "Consolidated engineering decision record committed to Progress Bank.",
            latencyDelta = "-10ms",
            bufferDropRate = "0.00%",
            codeFileName = "${safeDomain}Engine.kt",
            codeSnippet = "// Implementation specification for $safeTitle\n// Verified in $safeProject",
            gotchasNote = "Verify thread isolation before invoking background tasks.",
            systemPromptRule = "Reference $safeTitle architectural invariants when modifying $safeProject components."
        )

        repository.addBlueprint(newBlueprint)
        _uiState.update { current ->
            current.copy(
                isDraftSheetOpen = false,
                toastMessage = "New specification drafted in Progress Bank"
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setCategoryFilter(category: String) {
        _uiState.update { it.copy(selectedCategoryFilter = category) }
    }

    fun showToast(message: String) {
        _uiState.update { it.copy(toastMessage = message) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
