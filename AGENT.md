# ExpenseTracker — Agent instructions

**Project**: Expense Tracker
**Platform**: Android
**Architecture**: MVVM + Usecase
**Language**: Kotlin

---

## Project Overview
This is the Android application for tracking expenses on a daily basis.

### Main Goals
- Clean modular architecture
- Offline support
- Scalable feature modules
- High performance

## Tech Stack
- Kotlin
- Jetpack Compose
- MVVM + Clean Architecture
- Hilt for DI
- Retrofit + OkHttp
- Room DB
- Coroutines + Flow
- Gradle Kotlin DSL
- Use Hilt @Singleton only for shared services like DB, repositories.
  Avoid manual singleton objects.

## Repository Structure
/app                -> Main application module
/core/ui            -> Shared UI components
/core/network       -> API layer
/core/database      -> Room DB
/core/common        -> Utilities
/feature/*          -> Feature modules (e.g., splash, expenses, etc.)
/buildSrc           -> Build configs (if used)

## Architecture Rules
- UI (composables) must not call Retrofit, DAOs, or repositories directly; use ViewModels and use cases.
- Use ViewModel for screen logic.
- Use UseCases for business logic.
- Repository handles data source selection.
- Feature modules must not depend on each other directly.
- Shared code belongs in /core modules.

## Asset Management Rules

### Asset Ownership
- **Single canonical owner**: Every asset must have exactly one authoritative location.
- **Shared app-wide assets** → `app/src/main/assets/` (icons, app-level configs)
- **Feature-specific assets** → `feature/*/src/main/assets/` (feature animations, feature-specific media)
- **Shared UI assets** → `core/ui/src/main/assets/` (common themes, shared graphics)

### Refactoring Assets
- **Move, don't copy**: When relocating assets to new modules, move files completely.
- **Update all references**: Update import paths, resource references, and build configurations.
- **Clean up sources**: Delete original files after successful move and verification.
- **Verify functionality**: Ensure build passes and runtime asset loading works post-move.

### Duplicate Prevention
- **Pre-flight scan**: Before finalizing changes, scan for duplicate filenames across modules.
- **Unused asset detection**: Flag assets that are no longer referenced in code.
- **Conflict resolution**: Ask user before keeping multiple versions of same-named assets.
- **Empty directory cleanup**: Remove empty asset directories after moves.

### Module Refactor Checklist
For feature/module creation or restructuring:
- [ ] No duplicate assets exist
- [ ] No dead/unreferenced files remain
- [ ] Asset ownership follows module boundaries
- [ ] All asset references updated
- [ ] Build and runtime verification completed
- [ ] Empty directories removed

## Coding Standards
- Kotlin first.
- Prefer immutable data classes.
- Use StateFlow for UI state.
- Use sealed classes for screen states.
- Keep functions under 50 lines where possible.
- Use meaningful names.

## UI Guidelines
- Use Material3 theme.
- Reusable composables go to core/ui.
- Screen composables should be stateless where possible.
- Preview required for new composables.
- Use string resources only (no hardcoded text).
- Support dark mode.
- Support edge-to-edge layouts with proper system bar insets.

## Testing Rules
- Add unit tests for ViewModels and UseCases.
- Mock repositories, not Retrofit services.
- New business logic requires tests.
- UI tests only for critical flows.

## Dependency Rules
- Prefer existing libraries before adding new ones.
- Any new dependency must be justified.
- Use version catalog libs.versions.toml
- Avoid large unmaintained libraries.

## Gradle Configuration Rules

### Build System Setup
- **Always use KSP over KAPT**: KSP is faster and more compatible with modern Kotlin versions
- **Enable AndroidX**: Set `android.useAndroidX=true` in gradle.properties for all AndroidX dependencies
- **SDK Compatibility**: Use compatible compileSdk/targetSdk versions (35+ for AndroidX 1.15.0+)
- **Version Catalog**: Define all dependency versions in `gradle/libs.versions.toml`

### Plugin Management
- **Root Project Plugins**: Apply all plugins in root build.gradle.kts with `apply false`
- **Plugin Compatibility**: Test Kotlin/AGP/Hilt version combinations before upgrading
- **KSP Setup**: Use `ksp(libs.hilt.compiler)` instead of `kapt(libs.hilt.compiler)`

### Common Build Issues Prevention
- **AndroidX Migration**: Always enable AndroidX for projects using Compose/modern libraries  
- **SDK Mismatch**: Update compileSdk when dependencies require higher Android API levels
- **Plugin Conflicts**: Avoid mixing KAPT and KSP in the same project
- **Gradle Properties**: Include performance optimizations (configuration-cache, caching)

## Model Selection
| Model | When to use | Agents |
|-------|-------------|--------|
| `claude-opus-4-6` | Complex planning, architecture decisions, module boundaries, provider design | orchestrator, architect, kmm-researcher |
| `claude-sonnet-4-6` | Implementation, reviews, test writing, refactors, CI fixes | feature-builder, code-reviewer, test-writer, refactor-agent, ci-fixer |

## Multi module application
Modules should depend only inward:
feature -> domain -> core

## Build Commands
./gradlew assembleDebug
./gradlew test
./gradlew lint
./gradlew ktlintCheck

## Safe Changes
- UI screens
- ViewModels
- UseCases
- DAO additions
- Tests
- Asset moves within established module boundaries
- KSP annotation processor updates
- AndroidX dependency updates within same major version

## Dangerous Changes
- Gradle root config
- Room schema changes
- Major dependency upgrades (Kotlin, AGP, Compose BOM)
- Signing config
- CI pipeline
- Mass asset relocations without verification
- Deleting assets without confirming references
- KAPT to KSP migration without testing
- compileSdk downgrades when using newer AndroidX libraries

## Current Features
- Add Expense
- Expense List
- Category Selection
- Date Picker
- Monthly Summary
- Charts

## Database Rules
- Schema changes require migration
- DAO methods must be tested
- Index date column
- No database access from UI layer
- Use suspend/Flow queries

## Expense domain rules
- Amount > 0; category required.
- Date cannot be in the future unless explicitly “planned” / scheduled behavior.
- Store timestamps in UTC; show dates/times in the user’s local timezone.
- Money: `BigDecimal` or cents-based `Int`—never `Double` for persisted amounts.
- Currency formatting must be localized.
- List/sort expenses newest first by default.

## Continuous Improvement
Maintain and evolve agents and skills as the project grows.
Update them when:

- Repeated bugs or mistakes occur
- New coding patterns become standard
- New features/modules are introduced
- Architecture rules change
- Reviews reveal missing safeguards
- Manual repetitive work can be automated

Prefer improving existing agents/skills before adding new ones.
Keep responsibilities focused and avoid overlap.
