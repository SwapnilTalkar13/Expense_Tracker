---
name: architecture-guard
description: >-
  Detects architecture and layering violations for this repo using project rules
  in AGENT.md. Use when planning features, reviewing PRs, refactoring packages,
  or validating module boundaries—without duplicating stack or domain lists.
---

# Architecture guard

**Canonical rules** live in the repo root [`AGENT.md`](../../../AGENT.md) (tech stack, module graph, architecture, DB, DI, expense domain). Do not restate those lists here; read them when enforcing structure.

## Focus when auditing

- **Dependency direction**: `feature → domain → core`; no feature↔feature deps.
- **Layer boundaries**: UI does not touch Retrofit, DAOs, or repositories directly; business logic stays out of composables and belongs in use cases where applicable.
- **Data**: repositories own source selection; Room changes imply migrations and tests per AGENT.md.
- **Risk**: call out dangerous edits (Gradle root, schema, signing, CI) when relevant.

## Severity

| Level | Examples |
|-------|----------|
| **Blocker** | Wrong module dependency direction; DB/network access from UI; money as `Double` |
| **Warning** | Fat ViewModel; business logic in repository or UI |
| **Advisory** | Naming, packaging, duplication |

## Review behavior

1. Cite the violated rule (from AGENT.md).
2. Explain impact (coupling, testability, regressions).
3. Prefer the **smallest safe fix** that preserves behavior.