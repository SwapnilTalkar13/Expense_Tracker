package com.utility.expensetracker.feature.splash.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Simple unit tests for SplashViewModel focusing on core functionality
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelSimpleTest {
    private lateinit var viewModel: SplashViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SplashViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading with zero progress`() =
        runTest {
            // Given: Fresh ViewModel
            // When: Checking initial state
            // Then: Should be loading with 0 progress
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
            assertEquals(0f, viewModel.animationProgress.value)
        }

    @Test
    fun `completeSplash sets state to complete with full progress`() =
        runTest {
            // Given: ViewModel in loading state
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)

            // When: Manually completing splash
            viewModel.completeSplash()

            // Then: State should be complete with full progress
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }

    @Test
    fun `resetSplash resets state to loading with zero progress`() =
        runTest {
            // Given: Completed splash
            viewModel.completeSplash()
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)

            // When: Resetting splash
            viewModel.resetSplash()

            // Then: Should reset to loading with zero progress
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
            assertEquals(0f, viewModel.animationProgress.value)
        }

    @Test
    fun `configureSplashDuration allows setting custom duration`() =
        runTest {
            // Given: ViewModel
            val customDuration = 1000L

            // When: Configuring custom duration
            viewModel.configureSplashDuration(customDuration)

            // Then: No immediate change in state (duration affects future operations)
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
        }

    @Test
    fun `progress values stay within valid range`() =
        runTest {
            // Given: ViewModel
            viewModel.resetSplash()

            // When: Checking progress bounds
            val initialProgress = viewModel.animationProgress.value
            viewModel.completeSplash()
            val finalProgress = viewModel.animationProgress.value

            // Then: Progress should be within [0, 1] range
            assertTrue(initialProgress >= 0f && initialProgress <= 1f, "Initial progress out of bounds: $initialProgress")
            assertTrue(finalProgress >= 0f && finalProgress <= 1f, "Final progress out of bounds: $finalProgress")
            assertEquals(0f, initialProgress)
            assertEquals(1f, finalProgress)
        }
}
