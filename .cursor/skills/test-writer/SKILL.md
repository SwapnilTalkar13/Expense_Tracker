---
name: test-writer
description: >-
  Generates unit tests for ExpenseTracker ViewModels, use cases, and repositories
  following project patterns. Use when creating new business logic or reviewing
  test coverage gaps.
---

# Test writer

**Canonical rules** live in [`AGENT.md`](../../../AGENT.md) (Testing Rules). Reference those for mocking patterns and coverage expectations.

## Focus when writing tests

- **ViewModel tests**: Test state transitions, error handling, UI state updates.
- **UseCase tests**: Validate business logic, edge cases, domain rule enforcement.
- **Repository tests**: Mock data sources; test offline/online scenarios.
- **Mocking strategy**: Mock repositories, not Retrofit services; use test doubles for Room.

## Test patterns

**ViewModel structure:**
```kotlin
@Test
fun `when action X, then state updates to Y`() {
    // Given: initial state
    // When: action triggered  
    // Then: verify state change
}
```

**UseCase validation:**
```kotlin  
@Test
fun `when invalid input, then returns error`() {
    // Test domain rule enforcement
}
```

## Severity guidance

| Priority | Test focus |
|----------|------------|
| **Must have** | New business logic in use cases; ViewModel state changes |
| **Should have** | Repository error handling; edge case validation |
| **Nice to have** | UI tests for critical flows only |

## Review behavior

1. **Coverage check**: Identify untested business logic in new/modified code.
2. **Mock verification**: Ensure repositories are mocked, not external services.
3. **Test quality**: Verify clear arrange-act-assert structure and meaningful assertions.