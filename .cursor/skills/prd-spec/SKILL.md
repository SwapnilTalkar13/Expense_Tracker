---
name: prd-spec
description: >-
  Drafts concise PRDs and technical specs for ExpenseTracker features—problem,
  scope, UX notes, data/domain rules, analytics, and rollout risks—aligned with
  offline-first Android architecture. Use when the user asks for a PRD, spec,
  or feature brief before implementation.
---

# PRD / spec writing (ExpenseTracker)

## Document sections

1. **Problem & goal** — User outcome and success criteria.
2. **Scope** — In/out; links to screens or modules (`app`, `core/*`, future feature modules).
3. **User flows** — Primary paths; edge cases (offline, validation errors).
4. **Domain rules** — Amount > 0, category required, no future dates (unless “planned”), UTC storage, localized display, sort newest first.
5. **Data & architecture** — Entities, repositories, migrations if schema changes, API contracts if any.
6. **Non-goals & risks** — What ships later; perf/PII/testing notes.

## Style

- Short paragraphs and bullet lists; avoid boilerplate.
- Call out strings/theming (Material3, dark mode) and test expectations for ViewModels/use cases.
