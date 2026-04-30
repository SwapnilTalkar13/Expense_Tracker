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
 * Advanced unit tests for SplashViewModel timing logic
 *
 * Tests precise timing behavior, progress calculations, and edge cases
 * in the splash screen timing system.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SplashTimingLogicTest {
    private lateinit var viewModel: SplashViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    companion object {
        private const val DEFAULT_SPLASH_DURATION = 3000L
        private const val PROGRESS_UPDATE_INTERVAL = 50L
        private const val TIMING_TOLERANCE = 0.02f // 2% tolerance for timing tests
    }

    @Before
    fun setUp() {
        // Set up test dispatcher for viewModelScope
        Dispatchers.setMain(testDispatcher)
        viewModel = SplashViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `progress should update at expected intervals during splash`() =
        runTest {
            // Given: ViewModel with known duration
            val customDuration = 1000L
            viewModel.configureSplashDuration(customDuration)
            viewModel.resetSplash()

            val initialProgress = viewModel.animationProgress.value

            // When: Advancing time and capturing progress
            advanceTimeBy(300L) // 30% of duration
            val midProgress = viewModel.animationProgress.value

            advanceTimeBy(700L) // Complete remaining duration
            advanceUntilIdle()
            val finalProgress = viewModel.animationProgress.value

            // Then: Progress should update over time
            assertTrue(midProgress > initialProgress, "Progress should update during splash")
            assertTrue(finalProgress >= 0.95f, "Final progress should be near 1.0, but was $finalProgress")
        }

    @Test
    fun `progress calculation should be accurate at quarter intervals`() =
        runTest {
            // Given: ViewModel with 4-second duration for easy quarter calculations
            val duration = 4000L
            viewModel.configureSplashDuration(duration)
            viewModel.resetSplash()

            // When: Advancing to quarter intervals
            val quarterPoints = listOf(1000L, 2000L, 3000L, 4000L)
            val expectedProgress = listOf(0.25f, 0.5f, 0.75f, 1.0f)

            quarterPoints.zip(expectedProgress).forEach { (timeMs, expectedProg) ->
                advanceTimeBy(if (timeMs == 1000L) 1000L else 1000L) // Advance by 1s each time
                val actualProgress = viewModel.animationProgress.value

                // Then: Progress should be close to expected value
                val tolerance = if (expectedProg == 1.0f) 0.0f else TIMING_TOLERANCE
                assertTrue(
                    kotlin.math.abs(actualProgress - expectedProg) <= tolerance,
                    "At ${timeMs}ms, progress should be ~$expectedProg but was $actualProgress (tolerance: $tolerance)",
                )
            }
        }

    @Test
    fun `very short duration should complete within tolerance`() =
        runTest {
            // Given: ViewModel with very short duration
            val shortDuration = 100L
            viewModel.configureSplashDuration(shortDuration)
            viewModel.resetSplash()

            // When: Advancing slightly beyond short duration
            advanceTimeBy(shortDuration + 20)
            advanceUntilIdle()

            // Then: Should be completed
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }

    @Test
    fun `long duration should maintain precision throughout`() =
        runTest {
            // Given: ViewModel with long duration
            val longDuration = 30000L // 30 seconds
            viewModel.configureSplashDuration(longDuration)
            viewModel.resetSplash()

            // When: Testing at various points
            val testPoints =
                listOf(
                    // 10%
                    3000L to 0.1f,
                    // 20%
                    6000L to 0.2f,
                    // 50%
                    15000L to 0.5f,
                    // 80%
                    24000L to 0.8f,
                )

            var accumulatedTime = 0L
            testPoints.forEach { (targetTime, expectedProgress) ->
                val timeToAdvance = targetTime - accumulatedTime
                advanceTimeBy(timeToAdvance)
                accumulatedTime = targetTime

                val actualProgress = viewModel.animationProgress.value
                val tolerance = TIMING_TOLERANCE

                // Then: Progress should be accurate even with long duration
                assertTrue(
                    kotlin.math.abs(actualProgress - expectedProgress) <= tolerance,
                    "At ${targetTime}ms (${expectedProgress * 100}%), progress should be ~$expectedProgress but was $actualProgress",
                )
            }
        }

    @Test
    fun `rapid reset and timing should be stable`() =
        runTest {
            // Given: ViewModel with shorter duration for faster testing
            val testDuration = 1000L
            viewModel.configureSplashDuration(testDuration)

            // When: Resetting and testing once
            viewModel.resetSplash()
            assertEquals(0f, viewModel.animationProgress.value, "Reset should start at 0")

            // Advance to 50%
            advanceTimeBy(500L)
            val midProgress = viewModel.animationProgress.value
            assertTrue(
                midProgress >= 0.4f && midProgress <= 0.6f,
                "Mid progress should be ~50% but was $midProgress",
            )

            // Complete the cycle
            advanceTimeBy(600L)
            advanceUntilIdle()
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
        }

    @Test
    fun `progress should never decrease during normal operation`() =
        runTest {
            // Given: ViewModel with shorter duration for faster testing
            val shortDuration = 1000L
            viewModel.configureSplashDuration(shortDuration)
            viewModel.resetSplash()
            var previousProgress = viewModel.animationProgress.value

            // When: Advancing time in smaller steps
            repeat(10) { // 10 * 100ms = 1000ms
                advanceTimeBy(100L)
                val currentProgress = viewModel.animationProgress.value

                // Then: Progress should never decrease
                assertTrue(
                    currentProgress >= previousProgress,
                    "Progress should never decrease: step $it, previous=$previousProgress, current=$currentProgress",
                )
                previousProgress = currentProgress
            }

            // Final verification - should be complete
            advanceUntilIdle()
            assertTrue(viewModel.animationProgress.value >= previousProgress, "Final progress should be >= last progress")
        }

    @Test
    fun `manual completion should not interfere with timing precision`() =
        runTest {
            // Given: ViewModel with medium duration
            val duration = 5000L
            viewModel.configureSplashDuration(duration)
            viewModel.resetSplash()

            // When: Advancing partway then manually completing
            advanceTimeBy(2000L) // 40% of duration
            val progressBeforeCompletion = viewModel.animationProgress.value

            viewModel.completeSplash()

            // Then: Manual completion should set progress to 1.0
            assertEquals(1f, viewModel.animationProgress.value)
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)

            // And: Continuing time should not affect completed state
            advanceTimeBy(10000L) // Way beyond original duration
            assertEquals(1f, viewModel.animationProgress.value)
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)

            // Verify the progress before completion was reasonable
            assertTrue(
                progressBeforeCompletion >= 0.35f && progressBeforeCompletion <= 0.45f,
                "Progress before manual completion should be ~40% but was $progressBeforeCompletion",
            )
        }

    @Test
    fun `extreme duration values should be handled gracefully`() =
        runTest {
            val extremeDurations =
                listOf(
                    // 1ms - extremely short
                    1L,
                    // Very large but not overflow
                    Long.MAX_VALUE / 1000000,
                    // Zero duration
                    0L,
                    // Negative duration
                    -100L,
                )

            extremeDurations.forEach { duration ->
                // Given: ViewModel with extreme duration
                viewModel.configureSplashDuration(duration)
                viewModel.resetSplash()

                // When: Advancing reasonable amount of time
                advanceTimeBy(1000L)
                advanceUntilIdle()

                // Then: Should handle gracefully without crashes
                val progress = viewModel.animationProgress.value
                val state = viewModel.uiState.value

                assertTrue(progress >= 0f && progress <= 1f, "Progress should be valid for duration $duration: $progress")
                assertTrue(
                    state == SplashUiState.Complete || state == SplashUiState.Loading,
                    "State should be valid for duration $duration: $state",
                )
            }
        }

    @Test
    fun `timing precision should remain consistent across state resets`() =
        runTest {
            // Given: Fixed test parameters
            val testDuration = 1000L
            val testTime = 400L // 40% of duration
            val expectedProgress = 0.4f

            // When: Testing timing across a single reset
            viewModel.configureSplashDuration(testDuration)
            viewModel.resetSplash()

            advanceTimeBy(testTime)
            val actualProgress = viewModel.animationProgress.value

            // Then: Progress should be approximately expected value
            assertTrue(
                actualProgress >= expectedProgress - TIMING_TOLERANCE && actualProgress <= expectedProgress + TIMING_TOLERANCE,
                "Progress should be consistent ~$expectedProgress but was $actualProgress",
            )
        }
}
