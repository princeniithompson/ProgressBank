package com.example.data.repository

import com.example.data.model.Blueprint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BlueprintRepository {

    private val initialBlueprints = listOf(
        Blueprint(
            id = "audio-ducking-session",
            title = "Audio ducking & background speech session lifecycle",
            project = "VoiceMemo Pro",
            date = "Sep 14",
            domain = "audio",
            tags = listOf("AVAudioSession", "CoreAudio"),
            problemContext = "Audio glitching and audio rendering frame drops occur when re-activating AVAudioSession concurrently across background processing threads. System-level audio hardware renegotiation triggers micro-freezes during simultaneous live transcription buffers.",
            solutionSummary = "Implement explicit serial thread pinning using a dedicated audio synchronization actor, combined with a 300ms deferred deactivation guard. This prevents rapid teardown-restart thrashing during continuous speech pauses.",
            latencyDelta = "-42ms",
            bufferDropRate = "0.00%",
            codeFileName = "AVAudioSessionLifecycle.swift",
            codeSnippet = """let session = AVAudioSession.sharedInstance()
try session.setCategory(
    .playAndRecord,
    options: [.duckOthers, .defaultToSpeaker]
)
try session.setActive(true, options: [])""",
            gotchasNote = "Critical invariant: Never call setActive(false) synchronously inside audio capture callbacks or delegate notifications.",
            systemPromptRule = "When generating AVAudioEngine integration patches, always isolate teardown requests to AudioSessionActor.drainAndDeactivate() with dispatch after deadline."
        ),
        Blueprint(
            id = "text-injection-webview",
            title = "Text injection into sandboxed WebViews",
            project = "ReaderApp",
            date = "Aug 29",
            domain = "sandbox",
            tags = listOf("WebKit", "CSP Strict"),
            problemContext = "Script evaluation fails and content security policy violations are thrown when injecting reader highlights into sandboxed iframe boundaries with strict CSP headers.",
            solutionSummary = "Use postMessage messaging bridges with cryptographically signed origin handshakes instead of direct string script evaluations.",
            latencyDelta = "-18ms",
            bufferDropRate = "0.00%",
            codeFileName = "SandboxBridge.js",
            codeSnippet = """window.addEventListener('message', (event) => {
  if (event.origin !== TRUSTED_ORIGIN) return;
  injectReaderStyles(event.data.payload);
});""",
            gotchasNote = "Never bypass CSP nonce checks; validate the iframe origin before dispatching DOM mutations.",
            systemPromptRule = "Always generate structured postMessage handlers with origin validation when dealing with sandboxed WebView messaging."
        ),
        Blueprint(
            id = "low-latency-haptics",
            title = "Low-latency haptic feedback patterns",
            project = "HapticPad",
            date = "Aug 12",
            domain = "graphics",
            tags = listOf("CoreHaptics", "AHAP Specs"),
            problemContext = "Haptic engine spin-up introduces a 35-50ms perceptible lag when triggered directly upon user finger contact.",
            solutionSummary = "Prewarm the haptic actuator engine on touch down and deliver custom continuous transient patterns over pre-allocated hardware channels.",
            latencyDelta = "-35ms",
            bufferDropRate = "0.00%",
            codeFileName = "HapticDriver.swift",
            codeSnippet = """try engine.start()
let player = try engine.makeAdvancedPlayer(with: pattern)
try player.start(atTime: CHHapticTimeImmediate)""",
            gotchasNote = "Engine auto-stops after 10 seconds of inactivity; re-check engine status before playback.",
            systemPromptRule = "Prewarm CoreHaptics engines during navigation transitions to ensure immediate tap response."
        ),
        Blueprint(
            id = "offline-sync-crdt",
            title = "Offline sync with conflict-free registers",
            project = "SyncNotes",
            date = "Jul 03",
            domain = "sync",
            tags = listOf("LWW-Register", "SQLite"),
            problemContext = "Multi-device simultaneous offline edits result in destructive row overwrites upon reconnecting to the sync coordinator.",
            solutionSummary = "Equip each record with Last-Write-Wins (LWW) hybrid logical timestamps and deterministic tie-breaking by replica UUID.",
            latencyDelta = "-12ms",
            bufferDropRate = "0.00%",
            codeFileName = "ConflictResolver.kt",
            codeSnippet = """fun merge(local: Record, incoming: Record): Record {
    return if (incoming.hlc > local.hlc) incoming else local
}""",
            gotchasNote = "Device clocks can drift; never rely on raw wall-clock time without hybrid logical clock bounds.",
            systemPromptRule = "Use hybrid logical clocks with deterministic tie-breaking for all offline record sync models."
        ),
        Blueprint(
            id = "coregraphics-path-clipping",
            title = "CoreGraphics path clipping memory optimization",
            project = "SketchForge",
            date = "Jun 21",
            domain = "graphics",
            tags = listOf("CoreGraphics", "Vector Cache"),
            problemContext = "Repeated complex path clipping in high-resolution canvas redraw loops allocates excessive transient bitmap buffers.",
            solutionSummary = "Cache tessellated Bézier segments in reusable display list buffers and clip via vector stencil masks.",
            latencyDelta = "-28ms",
            bufferDropRate = "0.00%",
            codeFileName = "VectorRenderCache.swift",
            codeSnippet = """context.saveGState()
context.addPath(cachedClipPath)
context.clip()
drawTessellatedSegments(context)
context.restoreGState()""",
            gotchasNote = "Ensure saveGState and restoreGState balance exactly to prevent context stack overflow.",
            systemPromptRule = "Cache pre-computed vector paths rather than re-creating CGPath instances in inner draw loops."
        )
    )

    private val _blueprints = MutableStateFlow(initialBlueprints)
    val blueprints: StateFlow<List<Blueprint>> = _blueprints.asStateFlow()

    fun getBlueprintById(id: String): Blueprint? {
        return _blueprints.value.firstOrNull { it.id == id }
    }

    fun addBlueprint(blueprint: Blueprint) {
        _blueprints.update { current -> listOf(blueprint) + current }
    }

    fun search(query: String): List<Blueprint> {
        if (query.isBlank()) return _blueprints.value
        val lower = query.lowercase().trim()
        return _blueprints.value.filter {
            it.title.lowercase().contains(lower) ||
            it.project.lowercase().contains(lower) ||
            it.tags.any { tag -> tag.lowercase().contains(lower) } ||
            it.problemContext.lowercase().contains(lower)
        }
    }
}
