package com.utility.expensetracker.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

/**
 * ViewModel for managing splash screen state and timing
 *
 * Simple version without dependency injection for testing.
 */
class SplashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _animationProgress = MutableStateFlow(0f)
    val animationProgress: StateFlow<Float> = _animationProgress.asStateFlow()

    companion object {
        private const val PROGRESS_UPDATE_INTERVAL_MS = 50L
        private const val DEFAULT_SPLASH_DURATION = 3000L
    }

    private var splashDuration = DEFAULT_SPLASH_DURATION

    init {
        startSplashTimer()
    }

    /**
     * Starts the splash screen timer with progress tracking
     */
    private fun startSplashTimer() {
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                var currentTime = startTime

                while (currentTime - startTime < splashDuration) {
                    val elapsed = currentTime - startTime
                    val progress = min(elapsed.toFloat() / splashDuration, 1f)
                    _animationProgress.value = progress

                    delay(PROGRESS_UPDATE_INTERVAL_MS)
                    currentTime = System.currentTimeMillis()
                }

                _animationProgress.value = 1f
                _uiState.value = SplashUiState.Complete
            } catch (e: Exception) {
                _uiState.value =
                    SplashUiState.Error(
                        error = SplashError.LoadingFailed,
                        details = e.message,
                    )
            }
        }
    }

    /**
     * Manually complete the splash screen (useful for animation completion callbacks)
     */
    fun completeSplash() {
        _animationProgress.value = 1f
        _uiState.value = SplashUiState.Complete
    }

    /**
     * Configure splash duration (useful for customization or testing)
     *
     * Note: This will only affect future splash cycles, not the current one.
     */
    fun configureSplashDuration(durationMs: Long) {
        splashDuration = durationMs
    }

    /**
     * Reset splash state (useful for testing or if need to restart)
     */
    fun resetSplash() {
        _animationProgress.value = 0f
        _uiState.value = SplashUiState.Loading
        startSplashTimer()
    }
}
