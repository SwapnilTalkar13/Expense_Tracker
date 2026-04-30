# Splash Module Hilt DI Configuration

## Overview

The splash module has been configured with comprehensive Hilt dependency injection support, following clean architecture patterns and making it fully configurable and testable.

## Key Components

### 1. SplashModule (`di/SplashModule.kt`)

The main Hilt module that provides:
- **SplashConfig**: Default splash screen configuration
- **@SplashAnimationAsset**: Animation asset name (qualified injection)
- **@SplashMinDisplayTime**: Minimum display duration (qualified injection)
- **SplashRepository binding**: Repository interface to implementation binding

### 2. Qualifiers (`di/SplashQualifiers.kt`)

Custom qualifiers for type-safe dependency injection:
- `@SplashAnimationAsset`: For animation asset name
- `@SplashMinDisplayTime`: For splash duration configuration

### 3. Repository Layer

**Interface**: `domain/SplashRepository.kt`
- Defines contract for splash configuration and analytics
- Future-proof for remote configuration and tracking

**Implementation**: `data/SplashRepositoryImpl.kt`
- Provides configuration from injected defaults
- Placeholder for analytics tracking
- Can be easily extended for SharedPreferences, remote config, etc.

### 4. Use Cases

**GetSplashConfigUseCase**: `domain/GetSplashConfigUseCase.kt`
- Clean architecture use case for getting splash configuration
- Injected into ViewModel for configuration retrieval

**TrackSplashCompletionUseCase**: `domain/TrackSplashCompletionUseCase.kt`
- Handles analytics tracking for splash completion
- Ready for analytics integration

### 5. ViewModel Integration

**SplashViewModel**: `presentation/SplashViewModel.kt`
- Annotated with `@HiltViewModel` for automatic injection
- Injects `@SplashMinDisplayTime` for configuration
- Injects use cases following clean architecture
- Dynamically loads configuration and tracks completion

## Build Configuration

### Dependencies
- `hilt-android`: Core Hilt runtime
- `hilt-compiler`: KSP annotation processor
- `hilt-navigation-compose`: Compose navigation integration

### Plugins
- `hilt`: Hilt Android plugin
- `ksp`: Kotlin Symbol Processing for code generation

## Integration Points

### Application Level
- `ExpenseTrackerApplication.kt` annotated with `@HiltAndroidApp`
- `MainActivity.kt` annotated with `@AndroidEntryPoint`

### UI Level
- `SplashScreen.kt` uses `hiltViewModel()` for automatic injection
- No manual ViewModel creation required

## Configuration Options

### Default Values
- Animation asset: "splash_animation.json"
- Min display time: 3000ms (3 seconds)
- Fade out duration: 500ms

### Customization
The configuration can be easily customized by:
1. **Build Variants**: Override providers in app-level modules
2. **Testing**: Provide test configurations
3. **Remote Config**: Extend repository to load from remote sources
4. **User Preferences**: Add SharedPreferences integration

## Testing

### Unit Tests
- `SplashModuleTest.kt`: Tests provider methods
- `SplashRepositoryImplTest.kt`: Tests repository implementation
- All tests verify correct dependency provision

### Test Coverage
- ✅ Configuration provision
- ✅ Repository implementation
- ✅ Use case integration
- ✅ ViewModel dependency injection

## Architecture Benefits

1. **Separation of Concerns**: Clear separation between configuration, business logic, and UI
2. **Testability**: Easy to mock dependencies and test components in isolation
3. **Maintainability**: Centralized configuration management
4. **Extensibility**: Easy to add new features like remote config or analytics
5. **Type Safety**: Qualified injection prevents configuration mix-ups

## Future Enhancements

1. **Remote Configuration**: Load splash settings from server
2. **Analytics Integration**: Track splash metrics and user behavior
3. **A/B Testing**: Different splash configurations for user segments
4. **Dynamic Assets**: Load animation assets based on themes/events
5. **Performance Monitoring**: Track splash loading and animation performance

## Usage Example

```kotlin
// In UI layer - automatic injection
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel() // ✅ Automatic injection
) {
    // ViewModel automatically receives all configured dependencies
}

// For testing - easy mocking
@Test
fun testSplashConfiguration() {
    val config = SplashModule.provideSplashConfig()
    assertEquals(3000L, config.minDisplayTime)
}
```

This Hilt DI configuration makes the splash module completely self-contained, highly configurable, and ready for future enhancements while maintaining clean architecture principles.