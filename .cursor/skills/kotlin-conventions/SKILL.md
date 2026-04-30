---
name: kotlin-conventions
description: >-
  Applies ExpenseTracker Kotlin and Jetpack Compose conventions—immutability,
  StateFlow, sealed UI states, Material3, string resources, previews, and testing
  expectations. Use when writing or reviewing Kotlin/UI code in this repo.
---

# Kotlin & Compose conventions (ExpenseTracker)

## Language

- Prefer immutable `data class` models and clear naming.
- Use `StateFlow` for UI state; sealed classes for screen/load states.
- Keep functions focused; aim under ~50 lines for non-generated code.

## Compose / UI

- Material 3 theme; support dark mode.
- Reusable composables live under shared UI modules (`core/ui` pattern); screen composables stay stateless where possible.
- **No hardcoded user-visible strings**—use string resources.
- Add `@Preview` for new composables.

## Testing

- Unit-test ViewModels and use cases; mock repositories, not Retrofit services.
- New business logic should include tests.

## Money / expenses domain

- Amount > 0; category required; store times in UTC; display in local timezone.
- Use BigDecimal or cents-based `Int` for money; localize currency formatting.
