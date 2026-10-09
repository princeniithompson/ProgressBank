package com.example.data.model

/**
 * Technical blueprint definition representing a proven architectural pattern or fix.
 */
data class Blueprint(
    val id: String,
    val title: String,
    val project: String,
    val date: String,
    val domain: String,
    val tags: List<String>,
    val problemContext: String,
    val solutionSummary: String,
    val latencyDelta: String = "-42ms",
    val bufferDropRate: String = "0.00%",
    val codeFileName: String = "AVAudioSessionLifecycle.swift",
    val codeSnippet: String = """let session = AVAudioSession.sharedInstance()
try session.setCategory(
    .playAndRecord,
    options: [.duckOthers, .defaultToSpeaker]
)
try session.setActive(true, options: [])""",
    val gotchasNote: String = "Critical invariant: Never call setActive(false) synchronously inside audio capture callbacks or delegate notifications.",
    val systemPromptRule: String = "When generating AVAudioEngine integration patches, always isolate teardown requests to AudioSessionActor.drainAndDeactivate() with dispatch after deadline."
)
