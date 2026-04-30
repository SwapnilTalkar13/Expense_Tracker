# ExpenseTracker Asset Structure Guidelines

This document defines the canonical asset organization for the ExpenseTracker project.

## Asset Ownership Rules

### Single Source of Truth
Every asset file must have exactly **one canonical location**. Duplication across modules is prohibited except for deliberate versioning (which must be documented).

### Module-Based Ownership

#### App Module (`app/src/main/assets/`)
**Purpose**: App-wide assets shared across multiple features

**Contains**:
- App configuration files
- Global themes/branding assets  
- Launcher icons and splash screens (if not feature-specific)
- Shared data files (country codes, currencies, etc.)

**Examples**:
```
app/src/main/assets/
├── config/
│   ├── app-config.json
│   └── feature-flags.json
├── data/
│   ├── currencies.json
│   └── categories-default.json
└── branding/
    ├── app-logo.svg
    └── onboarding-images/
```

#### Feature Modules (`feature/*/src/main/assets/`)
**Purpose**: Assets used exclusively within a specific feature

**Current Features**:
- `feature/splash/src/main/assets/` - Splash screen animations
- `feature/expenses/src/main/assets/` - Expense-specific animations, icons
- `feature/analytics/src/main/assets/` - Chart templates, report assets

**Examples**:
```
feature/splash/src/main/assets/
└── splash_animation.json

feature/expenses/src/main/assets/
├── animations/
│   ├── expense-added.json
│   └── category-select.json
└── icons/
    ├── category-food.svg
    └── category-transport.svg
```

#### Core UI Module (`core/ui/src/main/assets/`)
**Purpose**: Shared UI components and reusable visual assets

**Contains**:
- Common component animations
- Shared icon sets
- UI theme-specific assets
- Loading animations used across features

**Examples**:
```
core/ui/src/main/assets/
├── animations/
│   ├── loading-spinner.json
│   └── success-checkmark.json
├── icons/
│   ├── common/
│   │   ├── arrow-back.svg
│   │   └── close.svg
│   └── navigation/
└── themes/
```

## Asset Migration Protocol

### When Moving Assets

1. **Identify true ownership**
   - Single feature usage → move to feature module
   - Multi-feature usage → keep in app or move to core/ui
   - Shared UI component → move to core/ui

2. **Execute clean move**
   ```bash
   # Move file completely
   mv app/src/main/assets/splash_animation.json feature/splash/src/main/assets/
   
   # Update all references in code
   # Update build.gradle.kts if needed
   ```

3. **Verify and cleanup**
   ```bash
   # Check build
   ./gradlew assembleDebug
   
   # Scan for duplicates
   find . -name "splash_animation.json"
   
   # Remove empty directories
   rmdir app/src/main/assets/ # if empty
   ```

## Build Configuration

### Asset Source Sets

Each module's `build.gradle.kts` should only reference its own assets:

```kotlin
// feature/splash/build.gradle.kts
android {
    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets")
        }
    }
}
```

### Asset Access Patterns

```kotlin
// ✅ Correct: Access own module's assets
class SplashScreen {
    private val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("splash_animation.json") // from own assets
    )
}

// ❌ Wrong: Accessing other module's assets directly
class ExpenseScreen {
    private val splash by rememberLottieComposition(
        LottieCompositionSpec.Asset("splash_animation.json") // not in expenses module!
    )
}
```

## Asset Naming Conventions

### File Naming
- Use kebab-case: `expense-added-animation.json`
- Include module context when ambiguous: `splash-wallet-animation.json`
- Version if necessary: `loading-spinner-v2.json`

### Directory Structure
```
assets/
├── animations/        # Lottie animations
├── icons/            # SVG/PNG icons
├── data/             # JSON configuration files
├── images/           # Static images
└── audio/            # Sound files (if needed)
```

## Validation Checklist

Before merging any asset-related changes:

### File Organization
- [ ] No duplicate files across modules
- [ ] Assets in correct module per ownership rules
- [ ] Empty directories cleaned up
- [ ] File names follow conventions

### Build Integrity
- [ ] `./gradlew assembleDebug` passes
- [ ] No missing resource errors
- [ ] Asset loading works at runtime
- [ ] APK size reasonable (no unnecessary duplicates)

### Code References
- [ ] All asset references updated
- [ ] Import paths correct
- [ ] Build configurations updated
- [ ] Documentation reflects changes

## Monitoring and Maintenance

### Regular Audits
Run monthly to maintain asset hygiene:

```bash
# Find potential duplicates
find . -name "*.json" -exec basename {} \; | sort | uniq -d

# Find unreferenced assets (manual review needed)
find . -name "*.json" | while read file; do
  filename=$(basename "$file")
  if ! rg -q "$filename" --type kotlin; then
    echo "Possibly unreferenced: $file"
  fi
done

# Find empty asset directories
find . -type d -name assets -empty
```

### Asset Size Monitoring
- Keep assets optimized (compress images, minify JSON)
- Monitor APK size impact of new assets
- Consider asset bundling for release builds

## Tools and Commands

### Asset Management Commands
```bash
# Asset inventory
find . -path "*/assets/*" -type f | sort

# Duplicate detection
find . -name "*.json" -exec basename {} \; | sort | uniq -d

# Reference search
rg "asset_name" --type kotlin

# Build verification
./gradlew clean assembleDebug
```

### Integration with CI
Consider adding asset validation to CI pipeline:
- Check for duplicates
- Validate asset references
- Monitor APK size changes