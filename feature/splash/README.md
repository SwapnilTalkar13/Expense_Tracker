# Splash Feature Module

A self-contained splash screen module for the ExpenseTracker app featuring Lottie animations and clean architecture.

## Features

- **Lottie Animation**: Wallet-themed splash animation
- **Clean Architecture**: Presentation layer with ViewModel and UI state management
- **Hilt Integration**: Dependency injection support
- **Material3 Theme**: Automatic dark/light mode support
- **Navigation Ready**: Composable navigation integration

## Usage

### Basic Integration

```kotlin
// In your navigation graph
splashScreen(
    onSplashComplete = { 
        navController.navigate("main_screen") 
    }
)
```

### Custom Configuration

The splash screen automatically displays for 3 seconds with the wallet animation. The duration can be modified in `SplashViewModel.kt`.

## Architecture

```
splash/
├── presentation/     # ViewModels and UI state
├── ui/              # Composable screens
├── navigation/      # Navigation setup
└── di/              # Hilt modules
```

## Dependencies

- Jetpack Compose
- Lottie Compose
- Hilt (Dependency Injection)
- Navigation Compose
- Material3

## Testing

The module includes:
- Unit tests for ViewModel timing logic
- Compose preview functions for UI verification
- Dark mode preview support