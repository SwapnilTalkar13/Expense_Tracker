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
- **Asset ownership**: verify single canonical asset locations per AGENT.md rules; flag duplicates.
- **Resource cleanup**: detect orphaned assets, empty directories after refactors.
- **Build configuration**: validate Gradle setup follows AGENT.md rules (KSP over KAPT, AndroidX enabled, compatible SDK versions).
- **Risk**: call out dangerous edits (Gradle root, schema, signing, CI) when relevant.

## Severity

| Level | Examples |
|-------|----------|
| **Blocker** | Wrong module dependency direction; DB/network access from UI; money as `Double`; duplicate assets across modules; KAPT + KSP mixing; AndroidX disabled with AndroidX dependencies |
| **Warning** | Fat ViewModel; business logic in repository or UI; assets in wrong module; incompatible SDK versions |
| **Advisory** | Naming, packaging, unused assets, empty directories; missing Gradle performance optimizations |

## Review behavior

1. Cite the violated rule (from AGENT.md).
2. Explain impact (coupling, testability, regressions).
3. Prefer the **smallest safe fix** that preserves behavior.