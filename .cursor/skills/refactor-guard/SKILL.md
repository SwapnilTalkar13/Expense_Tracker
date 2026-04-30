---
name: refactor-guard
description: >-
  Enforces clean asset management and file organization during module refactoring.
  Prevents duplicate assets, orphaned files, and maintains single source of truth
  for resources. Use when restructuring modules, moving features, or cleaning up
  project structure.
---

# Refactor Guard

Ensures clean, duplicate-free refactoring following ExpenseTracker asset management rules from [`AGENT.md`](../../../AGENT.md).

## Asset Movement Protocol

### Before Moving Assets

1. **Inventory current assets**
   ```bash
   find . -name "*.json" -o -name "*.png" -o -name "*.svg" | grep -E "(assets|res/drawable)"
   ```

2. **Map asset references**
   - Search codebase for asset file references
   - Document all import paths and resource IDs
   - Identify build.gradle.kts asset source sets

3. **Determine ownership**
   - **App-wide assets** → `app/src/main/assets/`
   - **Feature-specific assets** → `feature/*/src/main/assets/`
   - **Shared UI assets** → `core/ui/src/main/assets/`

### During Asset Move

1. **Move files completely** (don't copy)
2. **Update all references** in code and configs
3. **Update asset source paths** in build files
4. **Verify build configuration** includes new asset locations

### After Asset Move

1. **Delete original files** from old locations
2. **Remove empty directories** if safe
3. **Verify build passes** (`./gradlew assembleDebug`)
4. **Test asset loading** at runtime
5. **Scan for duplicates**

## Validation Checklist

Use this checklist for every refactoring session:

### File Organization
- [ ] No duplicate asset files across modules
- [ ] No orphaned/unreferenced assets
- [ ] Empty directories cleaned up
- [ ] Asset ownership follows module boundaries

### Reference Integrity
- [ ] All asset references updated
- [ ] Build configurations include new asset paths
- [ ] Import statements point to correct locations
- [ ] Resource IDs resolve correctly

### Build Verification
- [ ] `./gradlew assembleDebug` succeeds
- [ ] No missing resource errors
- [ ] Runtime asset loading works
- [ ] APK size didn't increase unnecessarily

## Common Violations

### Severity: Blocker
- Duplicate assets in multiple modules
- Asset references pointing to deleted files
- Build failures due to missing resources

### Severity: Warning
- Assets in wrong module (feature assets in app module)
- Unused assets consuming space
- Mixed asset organization patterns

### Severity: Advisory
- Empty asset directories
- Inconsistent asset naming
- Missing asset documentation

## Refactor Patterns

### Feature Module Creation
When extracting features from app module:
1. Identify feature-specific assets
2. Move assets to `feature/[name]/src/main/assets/`
3. Update feature module build.gradle.kts
4. Remove assets from app module
5. Verify no shared dependencies broken

### Core Module Refactoring
When moving shared assets to core modules:
1. Identify truly shared assets
2. Move to appropriate core module
3. Update all consuming modules' dependencies
4. Verify all features can still access assets

### Module Cleanup
Regular maintenance tasks:
1. Scan for unused assets with asset analysis
2. Remove orphaned files
3. Consolidate scattered similar assets
4. Update documentation

## Commands for Asset Management

```bash
# Find duplicate asset files
find . -name "*.json" -exec basename {} \; | sort | uniq -d

# Find empty asset directories  
find . -type d -name assets -empty

# Search for asset references in code
rg "splash_animation\.json" --type kotlin

# Verify build after asset moves
./gradlew clean assembleDebug
```

## Integration with Other Skills

- Use with `architecture-guard` for dependency validation
- Use with `kotlin-conventions` for proper resource handling
- Use with `compose-ui-guard` for UI asset organization