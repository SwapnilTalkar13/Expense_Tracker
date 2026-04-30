---
name: compose-ui-guard
description: >-
  Enforces Jetpack Compose and Material3 standards for ExpenseTracker UI—
  stateless composables, string resources, previews, dark mode. Use when
  reviewing UI code, new screens, or composable refactors.
---

# Compose UI guard

**Canonical rules** live in [`AGENT.md`](../../../AGENT.md) (UI Guidelines, Coding Standards). Reference those for complete UI patterns.

## Focus when auditing

- **State management**: Screen composables should be stateless; ViewModels expose `StateFlow`.
- **String resources**: No hardcoded user-visible text; use string resources only.
- **Material3**: Consistent theming; support dark mode.
- **Previews**: Required for new composables; show different states when applicable.
- **Reusability**: Shared composables belong in `core/ui`.

## Severity

| Level | Examples |
|-------|----------|
| **Blocker** | Hardcoded strings in UI; direct repository/DAO calls from composables |
| **Warning** | Missing previews; stateful screen composables; inconsistent Material3 usage |
| **Advisory** | Better composable naming; extract reusable components; preview variations |

## Review behavior

1. **String scan**: Search for hardcoded text in composables—flag for string resources.
2. **State isolation**: Verify composables receive data via parameters, not direct calls.
3. **Previews**: Check new composables have `@Preview` annotations.
4. **Theming**: Confirm Material3 colors/typography; test dark mode considerations.