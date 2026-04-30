---
name: build-guard
description: >-
  Validates Gradle build configuration and dependency management for ExpenseTracker.
  Prevents common build issues like plugin conflicts, SDK mismatches, and dependency
  resolution failures. Use when modifying build files, adding dependencies, or
  troubleshooting build failures.
---

# Build Configuration Guard

Ensures robust Gradle setup following ExpenseTracker build standards from [`AGENT.md`](../../../AGENT.md).

## Build Validation Checklist

### Plugin Configuration
- [ ] **KSP over KAPT**: Use KSP for annotation processing (Hilt, Room)
- [ ] **Root plugin declarations**: All plugins declared in root `build.gradle.kts` with `apply false`
- [ ] **Plugin compatibility**: Kotlin/AGP/Hilt versions are tested together
- [ ] **No plugin mixing**: Avoid KAPT + KSP in same project

### AndroidX Setup
- [ ] **AndroidX enabled**: `android.useAndroidX=true` in `gradle.properties`
- [ ] **Consistent SDK levels**: `compileSdk`/`targetSdk` compatible with AndroidX versions
- [ ] **Dependency resolution**: All AndroidX libraries resolve without conflicts

### Version Catalog (`gradle/libs.versions.toml`)
- [ ] **Centralized versions**: All dependency versions defined in catalog
- [ ] **Compatible versions**: Library versions tested for compatibility
- [ ] **Semantic versioning**: Follow semver for version management

## Common Build Issues & Solutions

### Issue: Plugin Resolution Errors
```
Error resolving plugin [id: 'org.jetbrains.kotlin.kapt']
```

**Diagnosis**:
- KAPT compatibility issues with newer Kotlin versions
- Plugin version conflicts in multi-module setup

**Solution**:
```kotlin
// Replace KAPT with KSP
// gradle/libs.versions.toml
ksp = "2.0.21-1.0.28"

// build.gradle.kts 
alias(libs.plugins.ksp)

// Dependencies
ksp(libs.hilt.compiler) // not kapt()
```

### Issue: AndroidX Dependency Conflicts
```
Configuration contains AndroidX dependencies, but android.useAndroidX property is not enabled
```

**Diagnosis**:
- Modern libraries require AndroidX support
- Legacy support library conflicts

**Solution**:
```properties
# gradle.properties
android.useAndroidX=true
```

### Issue: SDK Version Mismatches
```
Dependency requires libraries to compile against version 35 or later
```

**Diagnosis**:
- Newer AndroidX libraries require higher API levels
- `compileSdk` too low for dependency requirements

**Solution**:
```kotlin
android {
    compileSdk = 35  // Update to required level
    targetSdk = 35   // Optional: update target as needed
}
```

## Build Performance Optimizations

### Gradle Properties Recommendations
```properties
# Performance
org.gradle.configuration-cache=true
org.gradle.caching=true
org.gradle.parallel=true

# KSP optimizations
ksp.useKSP2=true

# Memory settings
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=1g
```

### Dependency Management
- **BOM usage**: Use Compose BOM for version alignment
- **Version catalogs**: Centralize all versions
- **Dependency analysis**: Regular cleanup of unused dependencies

## Validation Commands

### Pre-commit Checks
```bash
# Basic configuration validation
./gradlew help --no-daemon

# Dependency resolution check
./gradlew :app:dependencies --configuration debugRuntimeClasspath

# Build verification
./gradlew assembleDebug

# Clean build test
./gradlew clean assembleDebug
```

### Troubleshooting Commands
```bash
# Plugin resolution issues
./gradlew --refresh-dependencies

# Configuration cache issues
./gradlew --configuration-cache

# Detailed build info
./gradlew assembleDebug --info
```

## Module-Specific Validations

### New Module Checklist
- [ ] **Plugin setup**: Correct plugins for module type (library vs application)
- [ ] **SDK versions**: Match project-wide compileSdk/targetSdk
- [ ] **Dependencies**: Use project version catalog references
- [ ] **Build verification**: Module builds independently

### Feature Module Standards
```kotlin
// feature/*/build.gradle.kts template
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.utility.expensetracker.feature.*"
    compileSdk = 35  // Match project standard
    
    defaultConfig {
        minSdk = 26  // Project minimum
    }
    
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Use catalog references only
    implementation(libs.androidx.compose.ui)
    ksp(libs.hilt.compiler)  // KSP, not KAPT
}
```

## Integration with Other Skills

- Use with `architecture-guard` for comprehensive validation
- Use with `refactor-guard` when restructuring modules
- Use with `kotlin-conventions` for code style consistency

## Severity Levels

### Blocker
- Plugin resolution failures preventing build
- AndroidX conflicts breaking dependency resolution
- SDK version mismatches causing compilation failures

### Warning  
- Performance issues (missing optimizations)
- Deprecated plugin usage
- Inconsistent version catalog usage

### Advisory
- Missing build performance optimizations
- Unused dependency declarations
- Inconsistent module configuration patterns

## Continuous Monitoring

### Build Health Metrics
- Build success rate across all modules
- Build time trends (watch for performance regressions)
- Dependency conflict frequency
- Plugin update compatibility

### Regular Maintenance Tasks
- Quarterly dependency updates with compatibility testing
- Annual Gradle/plugin major version upgrades
- Monthly build performance analysis
- Build script cleanup and optimization