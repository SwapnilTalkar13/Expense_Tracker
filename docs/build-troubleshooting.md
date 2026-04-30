# Build Troubleshooting Guide

This guide covers common build issues in the ExpenseTracker project and their solutions.

## Quick Diagnostics

### Build Health Check
```bash
# 1. Basic configuration validation
./gradlew help --no-daemon

# 2. Dependency resolution check  
./gradlew :app:dependencies --configuration debugRuntimeClasspath

# 3. Clean build verification
./gradlew clean assembleDebug
```

## Common Issues & Solutions

### 1. Plugin Resolution Errors

#### Error: KAPT Plugin Compatibility
```
Error resolving plugin [id: 'org.jetbrains.kotlin.kapt', version: 'X.X.X']
The request for this plugin could not be satisfied because the plugin is already on the classpath
```

**Root Cause**: KAPT compatibility issues with newer Kotlin versions or plugin conflicts.

**Solution**: 
1. **Switch to KSP** (recommended):
   ```kotlin
   // gradle/libs.versions.toml
   ksp = "2.0.21-1.0.28"
   
   // build.gradle.kts
   plugins {
       alias(libs.plugins.ksp)  // instead of kotlin.kapt
   }
   
   dependencies {
       ksp(libs.hilt.compiler)  // instead of kapt()
   }
   ```

2. **Alternative**: Downgrade Kotlin version to compatible with KAPT

#### Error: Plugin Not Found
```
Plugin [id: 'com.example.plugin'] was not found in any of the following sources
```

**Solution**:
1. Check plugin is declared in root `build.gradle.kts` with `apply false`
2. Verify plugin version exists in Gradle Plugin Portal
3. Add plugin repository if needed

### 2. AndroidX Dependency Issues

#### Error: AndroidX Not Enabled
```
Configuration contains AndroidX dependencies, but the android.useAndroidX property is not enabled
```

**Solution**:
```properties
# gradle.properties
android.useAndroidX=true
```

#### Error: Support Library Conflicts
```
All com.android.support libraries must use the exact same version specification
```

**Solution**: 
1. Enable AndroidX migration
2. Remove all legacy support library dependencies
3. Use AndroidX equivalents only

### 3. SDK Version Mismatches

#### Error: Dependency Requires Higher API Level
```
Dependency 'androidx.core:core:1.15.0' requires libraries and applications that
depend on it to compile against version 35 or later of the Android APIs
```

**Solution**:
```kotlin
android {
    compileSdk = 35  // Update to required level
    targetSdk = 35   // Optional: update target as needed
}
```

**Prevention**: Check dependency requirements before updating AndroidX versions.

### 4. Dependency Resolution Failures

#### Error: Version Conflicts
```
Conflict with dependency 'org.jetbrains.kotlin:kotlin-stdlib'
```

**Solution**:
1. **Force consistent versions** in version catalog:
   ```toml
   [versions]
   kotlin = "2.0.21"  # Use same version everywhere
   ```

2. **Use BOM for version alignment**:
   ```kotlin
   dependencies {
       implementation(platform(libs.androidx.compose.bom))
   }
   ```

#### Error: Missing Dependencies
```
Could not resolve androidx.navigation:navigation-compose:X.X.X
```

**Solution**:
1. **Verify version exists**:
   ```bash
   # Check available versions
   ./gradlew dependencyInsight --dependency androidx.navigation:navigation-compose
   ```

2. **Update version catalog**:
   ```toml
   androidxNavigation = "2.8.5"  # Use stable version
   ```

### 5. Build Performance Issues

#### Error: Out of Memory
```
java.lang.OutOfMemoryError: GC overhead limit exceeded
```

**Solution**:
```properties
# gradle.properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=1g -XX:+UseParallelGC
```

#### Error: Slow Configuration Time
```
Configuration cache problems found in this build
```

**Solution**:
```properties
# gradle.properties
org.gradle.configuration-cache=true
org.gradle.caching=true
org.gradle.parallel=true
```

## Module-Specific Issues

### Feature Module Build Failures

#### Error: Duplicate Class
```
Duplicate class found in modules
```

**Solution**:
1. **Check asset duplication** (follow refactor-guard rules)
2. **Verify module dependencies** don't create cycles
3. **Use proper dependency scoping** (implementation vs api)

#### Error: Resource Conflicts
```
Resource and asset merger errors
```

**Solution**:
1. **Move assets to correct ownership** (per asset-structure guidelines)
2. **Remove duplicate resources** across modules
3. **Use unique resource naming** with module prefixes

### Hilt Integration Issues

#### Error: Missing Application Class
```
@HiltAndroidApp class not found
```

**Solution**:
1. **Add Application class**:
   ```kotlin
   @HiltAndroidApp
   class ExpenseTrackerApplication : Application()
   ```

2. **Register in AndroidManifest.xml**:
   ```xml
   <application
       android:name=".ExpenseTrackerApplication"
       ... >
   ```

#### Error: Component Generation Failures
```
Hilt component generation failed
```

**Solution**:
1. **Verify KSP setup** (not KAPT)
2. **Check annotation processor classpath**
3. **Clean and rebuild**: `./gradlew clean assembleDebug`

## Build Environment Issues

### Gradle Version Compatibility

#### Error: Gradle Version Too Old
```
This version of the Android Gradle Plugin requires Gradle X.X or higher
```

**Solution**:
```properties
# gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
```

#### Error: Java Version Mismatch
```
Unsupported Java version
```

**Solution**:
1. **Check Java version**: `java -version`
2. **Update compileOptions**:
   ```kotlin
   compileOptions {
       sourceCompatibility = JavaVersion.VERSION_17
       targetCompatibility = JavaVersion.VERSION_17
   }
   ```

### CI/CD Build Issues

#### Error: Missing Build Tools
```
Failed to find Build Tools revision X.X.X
```

**Solution**:
1. **Use stable SDK versions** in CI
2. **Install required build tools** in CI setup
3. **Lock SDK versions** in build configuration

## Prevention Best Practices

### Before Adding Dependencies
1. ✅ Check AndroidX compatibility
2. ✅ Verify SDK requirements  
3. ✅ Test in isolated branch
4. ✅ Update version catalog
5. ✅ Run full build verification

### Before Module Changes
1. ✅ Read build-guard and refactor-guard skills
2. ✅ Plan asset ownership changes
3. ✅ Verify dependency directions
4. ✅ Test build in clean environment

### Regular Maintenance
1. 🔄 **Weekly**: Build health checks
2. 🔄 **Monthly**: Dependency analysis and cleanup  
3. 🔄 **Quarterly**: Major dependency updates with testing
4. 🔄 **Annually**: Gradle/plugin major version upgrades

## Emergency Recovery

### Nuclear Option - Reset Build
```bash
# 1. Stop all Gradle processes
./gradlew --stop
pkill -f gradle

# 2. Clean all caches
rm -rf ~/.gradle/caches
rm -rf .gradle
rm -rf */build

# 3. Reset to known good state
git clean -fdx
./gradlew clean assembleDebug
```

### Rollback Strategy
1. **Git rollback** to last known good commit
2. **Incremental changes** to identify the breaking change
3. **Targeted fix** for specific issue
4. **Full build verification** before proceeding

## Getting Help

### Build Scan Analysis
```bash
./gradlew assembleDebug --scan
```
Use build scans to analyze performance and identify bottlenecks.

### Gradle Debug Info
```bash
./gradlew assembleDebug --info --debug
```
Get detailed information about build process and failures.

### Community Resources
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Android Gradle Plugin Release Notes](https://developer.android.com/build/releases/gradle-plugin)
- [Kotlin Releases](https://kotlinlang.org/releases.html)
- [Hilt Documentation](https://dagger.dev/hilt/)