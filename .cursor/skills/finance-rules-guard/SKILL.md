---
name: finance-rules-guard
description: >-
  Validates expense business rules and money handling for ExpenseTracker—amount
  validation, currency safety, date constraints. Use when reviewing expense
  creation, editing, or financial calculations.
---

# Finance rules guard

**Canonical rules** live in [`AGENT.md`](../../../AGENT.md) (Expense domain rules). Reference those for complete validation logic.

## Focus when auditing

- **Amount validation**: Must be > 0; no negative expenses without explicit "refund" context.
- **Money safety**: `BigDecimal` or cents-based `Int`; never `Double` for persisted amounts.
- **Date constraints**: No future dates unless explicitly "planned" functionality.
- **Category enforcement**: Category required for all expenses.
- **Currency**: Localized formatting; consistent precision handling.

## Severity

| Level | Examples |
|-------|----------|
| **Blocker** | Money stored as `Double`; negative amounts accepted; missing amount validation |
| **Warning** | Hardcoded currency symbols; inconsistent decimal precision; missing category validation |
| **Advisory** | Better validation error messages; localization improvements |

## Review behavior

1. **Money types**: Scan for `Double` in expense amounts—flag immediately.
2. **Validation**: Confirm amount > 0 and category checks exist.
3. **Precision**: Verify consistent decimal handling in calculations.
4. **Edge cases**: Test boundary conditions (very small amounts, large numbers).