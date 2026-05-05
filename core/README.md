# Core Modules Structure

This directory contains the core modules that provide shared functionality across the ExpenseTracker application.

## Module Overview

### :core:common
- **Purpose**: Common utilities, extensions, and constants
- **Contents**: 
  - Application constants
  - Flow extensions for Result wrapping
  - Shared utility functions

### :core:domain
- **Purpose**: Domain models and repository interfaces (business logic layer)
- **Contents**:
  - Domain models (`Category`, `Expense`, `CategoryWithExpenseCount`)
  - Repository interfaces for data operations
  - Business rules and domain logic

### :core:database
- **Purpose**: Database layer with Room persistence (currently stubbed)
- **Contents**:
  - Database entities
  - DAO interfaces
  - Database configuration
- **Status**: ⚠️ Room implementation temporarily disabled due to KSP compatibility issues

### :core:data
- **Purpose**: Repository implementations and data mapping
- **Contents**:
  - Repository implementations
  - Entity ↔ Domain model mappers
  - Data source coordination
- **Status**: ⚠️ Hilt DI temporarily disabled pending Room implementation

## Dependencies

The modules follow the clean architecture dependency rule:
```
:app → :feature:* → :core:data → :core:domain
                              ↘ :core:database
                    :core:common ← (used by all)
```

## Current Status

✅ **Completed**:
- Module structure created and building successfully
- Basic domain models and interfaces defined
- Repository pattern established
- Gradle build configuration

🚧 **Pending** (Room/KSP compatibility issue):
- Room database implementation
- Hilt dependency injection setup
- Complete DAO implementations

## Next Steps

1. Resolve KSP/Room compatibility issue
2. Re-enable Room annotations and database setup
3. Re-enable Hilt DI modules
4. Add database migration support
5. Implement default category seeding