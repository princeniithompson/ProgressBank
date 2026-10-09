# Progress Bank

Progress Bank is a native Android application built with Jetpack Compose and Material 3 Expressive, designed as a personal smart notebook for app-building blueprints (saved architectural patterns, text injection, audio session lifecycles, low-latency haptics, offline CRDT synchronization, etc.).

## Visual Design & Signature Look
- **Palette**: Deep navy & indigo brand tokens (`#0F1E2C`), light lavender-tinted surfaces (`#FEF7FF`), clean slate containers (`#CEE2F6`).
- **Signature Accent**: Blended Indigo Gradient on primary user bubbles, send action buttons, and spec drafting CTA.
- **Top Navigation**: Segmented tab control (`Ask` | `New Progress`) with no bottom navigation bar.
- **House Layout Rules**: Screen roots strictly use `Box(Modifier.fillMaxSize().background(...))` (never Scaffold), circles and status rings drawn using `Canvas.drawCircle` (never clipped Boxes), full edge-to-edge support with `enableEdgeToEdge()`.

## Core Features

### 1. Ask Tab
- **Empty State**: Friendly hero with prompt suggestions ("Cold start latency", "Connection pool fix", "Token refresh race") to populate the composer instantly.
- **Test Mode Toggle**: Small top toggle (`Match` / `No match`) allowing preview of both retrieval states without external backend dependency.
- **Match State**:
  - User query rendered exclusively in a blended indigo gradient bubble on the right (tap to expand).
  - Synthesized application answer rendered as clean raw text directly on the canvas background.
  - Linked blueprint spec card with an **Open** button that navigates directly to the blueprint's detail specification.
  - Quick context action pill: "Copy as agent prompt".
- **No-Match State**:
  - Center upper-middle circular `+` button rendered via Canvas.
  - "Add to progress" headline with "Nothing similar in your progress" notice.
  - Flat title-only list of existing blueprints with chevrons to browse related solutions.
- **Fixed Bottom Composer**: Pinned query input dock with comfortable touch targets and blended indigo send button.

### 2. New Progress Tab
- Vault index of saved architectural blueprints.
- Quick filter chips (`All Specs`, `Audio`, `Web & Sandbox`, `Hardware & UI`, `Data & CRDT`).
- Real-time search query filtering over titles, projects, and tags.
- Floating "Draft Spec" action button and top "+ New Spec" control that opens a bottom sheet to commit new architectural entries.

### 3. Milestone / Blueprint Detail Screen
- Technical spec and agent context modes.
- Full problem context and solution summary with metric deltas.
- Inline Session Pipeline State Machine vector diagram.
- Terminal-styled code block with filename and one-tap clipboard copy.
- Gotchas & Agent prompt notes for LLM coding workflows.
- Native back navigation handling via `BackHandler`.

## Architecture & Directory Structure

```
app/src/main/java/com/example/
├── MainActivity.kt                 # Single Activity entry point with edge-to-edge initialization
├── core/
│   └── AppConfig.kt                # Application configuration constants and defaults
├── data/
│   ├── model/
│   │   └── Blueprint.kt            # Blueprint entity data model
│   └── repository/
│       └── BlueprintRepository.kt  # In-memory repository with preloaded sample blueprints
├── viewmodel/
│   ├── ProgressBankUiState.kt      # Immutable UI state and enums (AppTab, TestMode)
│   └── ProgressBankViewModel.kt    # MVVM StateFlow management and actions
└── ui/
    ├── ask/
    │   ├── AskEmptyHero.kt         # First-open suggestions and search hero
    │   ├── AskMatchContent.kt      # Match state (user bubble, raw answer, spec card)
    │   ├── AskNoMatchContent.kt    # No-match state (+ hero, flat list)
    │   └── AskScreen.kt            # Ask tab container with fixed bottom composer
    ├── components/
    │   ├── BlueprintRowItem.kt     # Flat row and card item composables
    │   ├── CircularAddButton.kt    # Canvas-drawn large circular add button
    │   ├── TopHeaderBar.kt         # App bar with bank icon, notifications, and profile
    │   └── TopTabSegment.kt        # Segmented top tab switcher (Ask | New Progress)
    ├── detail/
    │   └── BlueprintDetailScreen.kt # Full milestone detail with state machine & code snippet
    ├── newprogress/
    │   ├── DraftBlueprintSheet.kt  # Modal sheet for drafting new blueprints
    │   └── NewProgressScreen.kt    # Vault index with search, filters, and list
    ├── main/
    │   └── MainScreen.kt           # Screen orchestrator, crossfade tabs, and toast notifications
    └── theme/
        ├── Color.kt                # Material 3 light/dark color tokens
        ├── Gradients.kt            # Named theme gradient brushes (Blended Indigo)
        ├── Theme.kt                # ProgressBankTheme definition
        └── Type.kt                 # Material 3 Typography scale
```
