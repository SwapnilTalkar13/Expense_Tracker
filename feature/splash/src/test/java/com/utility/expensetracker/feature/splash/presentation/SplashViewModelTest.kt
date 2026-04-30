package com.utility.expensetracker.feature.splash.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for SplashViewModel
 *
 * Tests state transitions, timing logic, animation progress, and error handling.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {
    private lateinit var viewModel: SplashViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        // Set up test dispatcher for viewModelScope
        Dispatchers.setMain(testDispatcher)
        // Create ViewModel with test dispatcher
        viewModel = SplashViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading with zero progress`() =
        runTest {
            // Given: Fresh ViewModel instance
            // When: Checking initial state
            // Then: State should be loading with 0 progress
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
            assertEquals(0f, viewModel.animationProgress.value)
        }

    @Test
    fun `when splash timer starts, then animation progress increases over time`() =
        runTest {
            // Given: Fresh ViewModel instance
            val initialProgress = viewModel.animationProgress.value

            // When: Advancing time by 500ms
            advanceTimeBy(500)

            // Then: Progress should have increased
            val progressAfter500ms = viewModel.animationProgress.value
            assertTrue(
                progressAfter500ms > initialProgress,
                "Progress should increase after 500ms: $progressAfter500ms > $initialProgress",
            )
        }

    @Test
    fun `when splash timer completes naturally, then state becomes complete with full progress`() =
        runTest {
            // Given: Fresh ViewModel instance
            // When: Advancing time beyond default duration (3000ms)
            advanceTimeBy(3100) // Slightly more than default duration
            advanceUntilIdle()

            // Then: State should be complete with full progress
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }

    @Test
    fun `when completeSplash is called manually, then state becomes complete immediately`() =
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
    fun `when configureSplashDuration is called, then future cycles use new duration`() =
        runTest {
            // Given: ViewModel with default duration
            val customDuration = 1000L

            // When: Configuring custom duration and resetting
            viewModel.configureSplashDuration(customDuration)
            viewModel.resetSplash()

            // Then: Splash should complete faster with custom duration
            advanceTimeBy(1100) // Slightly more than custom duration
            advanceUntilIdle()

            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }

    @Test
    fun `when resetSplash is called, then state resets to loading with zero progress`() =
        runTest {
            // Given: ViewModel that has completed
            viewModel.completeSplash()
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)

            // When: Resetting splash
            viewModel.resetSplash()

            // Then: State should reset to loading with zero progress
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
            assertEquals(0f, viewModel.animationProgress.value)
        }

    @Test
    fun `animation progress should never exceed 1_0`() =
        runTest {
            // Given: ViewModel with very long duration
            viewModel.configureSplashDuration(10000L)
            viewModel.resetSplash()

            // When: Advancing time well beyond duration
            advanceTimeBy(15000)
            advanceUntilIdle()

            // Then: Progress should be capped at 1.0
            assertEquals(1f, viewModel.animationProgress.value)
            assertTrue(viewModel.animationProgress.value <= 1f)
        }

    @Test
    fun `animation progress should be proportional to elapsed time`() =
        runTest {
            // Given: ViewModel with known duration
            val testDuration = 2000L
            viewModel.configureSplashDuration(testDuration)
            viewModel.resetSplash()

            // When: Advancing time to 25% of duration
            advanceTimeBy(500) // 25% of 2000ms
            advanceUntilIdle()

            // Then: Progress should be approximately 25%
            val progress = viewModel.animationProgress.value
            assertTrue(
                progress >= 0.2f && progress <= 0.3f,
                "Progress should be around 25% but was $progress",
            )
        }

    @Test
    fun `when advancing time incrementally, then progress increases monotonically`() =
        runTest {
            // Given: ViewModel with standard duration
            viewModel.resetSplash()
            val initialProgress = viewModel.animationProgress.value

            // When: Advancing time incrementally
            advanceTimeBy(500) // First increment
            val midProgress = viewModel.animationProgress.value

            advanceTimeBy(500) // Second increment
            val laterProgress = viewModel.animationProgress.value

            // Then: Progress should always increase or stay the same
            assertTrue(
                midProgress >= initialProgress,
                "Mid progress should be >= initial: $midProgress >= $initialProgress",
            )
            assertTrue(
                laterProgress >= midProgress,
                "Later progress should be >= mid: $laterProgress >= $midProgress",
            )
        }

    @Test
    fun `multiple resetSplash calls should work correctly`() =
        runTest {
            // Given: ViewModel
            // When: Resetting once and testing
            viewModel.resetSplash()
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
            assertEquals(0f, viewModel.animationProgress.value)

            // Advance a bit and verify progress
            advanceTimeBy(500)
            assertTrue(viewModel.animationProgress.value > 0f)

            // Then: Another reset should still work
            viewModel.resetSplash()
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
            assertEquals(0f, viewModel.animationProgress.value)
        }

    @Test
    fun `when splash completes manually before timer, then timer should not override state`() =
        runTest {
            // Given: ViewModel with longer duration
            viewModel.configureSplashDuration(5000L)
            viewModel.resetSplash()

            // When: Manually completing splash early
            advanceTimeBy(1000)
            viewModel.completeSplash()

            // Then: State should remain complete even after more time
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)

            advanceTimeBy(5000) // Advance past original duration
            advanceUntilIdle()

            // Should still be complete
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }

    @Test
    fun `configureSplashDuration with zero duration should complete immediately on reset`() =
        runTest {
            // Given: ViewModel with zero duration
            viewModel.configureSplashDuration(0L)

            // When: Resetting splash
            viewModel.resetSplash()
            advanceUntilIdle()

            // Then: Should complete immediately
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }

    @Test
    fun `configureSplashDuration with negative duration should be handled gracefully`() =
        runTest {
            // Given: ViewModel with negative duration
            viewModel.configureSplashDuration(-1000L)

            // When: Resetting splash
            viewModel.resetSplash()
            advanceUntilIdle()

            // Then: Should complete immediately (negative duration treated as 0)
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }

    @Test
    fun `when very small duration is set, then splash should still complete properly`() =
        runTest {
            // Given: ViewModel with very small duration
            viewModel.configureSplashDuration(1L) // 1 millisecond
            viewModel.resetSplash()

            // When: Advancing minimal time
            advanceTimeBy(2)
            advanceUntilIdle()

            // Then: Should complete
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }
}
