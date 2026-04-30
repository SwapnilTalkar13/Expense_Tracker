---
name: room-db-guard
description: >-
  Enforces Room database safety, migration planning, and DAO patterns for
  ExpenseTracker. Use when reviewing DB schema changes, DAO methods, or
  persistence layer refactors.
---

# Room DB guard

**Canonical rules** live in [`AGENT.md`](../../../AGENT.md) (Database Rules, Architecture Rules). Reference those for complete context.

## Focus when auditing

- **Schema safety**: Changes require migrations; validate migration paths before merge.
- **DAO patterns**: Return `suspend` functions or `Flow`; no blocking calls; test new queries.
- **Layer isolation**: No DB access from UI layer; repositories own DAO interactions.
- **Performance**: Index frequently queried columns (dates, categories); avoid N+1 patterns.

## Severity

| Level | Examples |
|-------|----------|
| **Blocker** | Schema change without migration; DAO called from UI; blocking DB calls |
| **Warning** | Missing indexes on query columns; untested DAO methods; complex queries without explanation |
| **Advisory** | Query optimization opportunities; better entity naming |

## Review behavior

1. **Schema changes**: Verify migration exists and is tested.
2. **New DAOs**: Confirm `suspend`/`Flow` patterns and unit tests.
3. **Performance**: Check indexes for new query patterns.
4. **Safety**: Flag direct DB access from ViewModels or UI.