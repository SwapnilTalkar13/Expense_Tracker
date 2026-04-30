package com.utility.expensetracker.feature.splash.performance

import com.utility.expensetracker.feature.splash.presentation.SplashUiState
import com.utility.expensetracker.feature.splash.presentation.SplashViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
 * Practical performance validation tests for splash screen
 *
 * Focuses on validating key performance characteristics:
 * - Initialization speed
 * - State transition efficiency
 * - Animation timing accuracy
 * - Resource usage patterns
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SplashPerformanceValidationTest {
    private lateinit var viewModel: SplashViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel initialization should be fast and lightweight`() =
        runTest {
            // Given: Measuring initialization time
            val startTime = System.currentTimeMillis()

            // When: Creating ViewModel
            viewModel = SplashViewModel()
            val initTime = System.currentTimeMillis() - startTime

            // Then: Should initialize quickly and have correct initial state
            assertTrue(initTime < 100L, "ViewModel init took ${initTime}ms - should be under 100ms")
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
            assertEquals(0f, viewModel.animationProgress.value)
        }

    @Test
    fun `animation progress should update smoothly during splash duration`() =
        runTest {
            // Given: ViewModel with known duration
            viewModel = SplashViewModel()
            viewModel.configureSplashDuration(1000L) // 1 second
            viewModel.resetSplash()

            val progressSnapshots = mutableListOf<Float>()

            // When: Capturing progress at regular intervals
            repeat(10) { step ->
                advanceTimeBy(100L) // 100ms intervals
                progressSnapshots.add(viewModel.animationProgress.value)
            }

            advanceUntilIdle()

            // Then: Progress should generally increase and reach completion
            val firstProgress = progressSnapshots.first()
            val lastProgress = progressSnapshots.last()

            assertTrue(firstProgress >= 0f, "Initial progress should be non-negative: $firstProgress")
            assertTrue(lastProgress > firstProgress, "Progress should increase: $firstProgress -> $lastProgress")
            assertEquals(SplashUiState.Complete, viewModel.uiState.value, "Should reach completed state")
            assertEquals(1f, viewModel.animationProgress.value, "Should reach 100% progress")
        }

    @Test
    fun `timing accuracy should be within acceptable variance`() =
        runTest {
            // Given: ViewModel with precise duration
            viewModel = SplashViewModel()
            val testDuration = 2000L
            viewModel.configureSplashDuration(testDuration)
            viewModel.resetSplash()

            // When: Running for expected duration
            advanceTimeBy(testDuration)
            advanceUntilIdle()

            // Then: Should complete within expected timeframe
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)

            // Test 50% progress point
            viewModel.resetSplash()
            advanceTimeBy(testDuration / 2)
            val halfwayProgress = viewModel.animationProgress.value

            // Allow 10% variance for timing precision
            assertTrue(
                halfwayProgress >= 0.4f && halfwayProgress <= 0.6f,
                "At 50% duration, progress should be ~50% but was ${halfwayProgress * 100}%",
            )
        }

    @Test
    fun `state transitions should be immediate and consistent`() =
        runTest {
            // Given: ViewModel ready for state operations
            viewModel = SplashViewModel()
            viewModel.configureSplashDuration(5000L) // Long duration
            viewModel.resetSplash()

            // When: Testing manual completion
            advanceTimeBy(1000L) // Advance partway
            val beforeCompletion = viewModel.uiState.value
            val progressBeforeCompletion = viewModel.animationProgress.value

            viewModel.completeSplash()

            // Then: Manual completion should be immediate
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
            assertEquals(SplashUiState.Loading, beforeCompletion, "Should have been loading before completion")
            assertTrue(progressBeforeCompletion < 1f, "Should have been in progress before completion")
        }

    @Test
    fun `reset functionality should restore initial state efficiently`() =
        runTest {
            // Given: ViewModel that has progressed
            viewModel = SplashViewModel()
            viewModel.configureSplashDuration(1000L)
            viewModel.resetSplash()

            advanceTimeBy(500L) // Progress halfway
            val midProgress = viewModel.animationProgress.value

            // When: Resetting splash
            viewModel.resetSplash()

            // Then: Should return to initial state
            assertEquals(SplashUiState.Loading, viewModel.uiState.value)
            assertEquals(0f, viewModel.animationProgress.value)
            assertTrue(midProgress > 0f, "Should have progressed before reset: $midProgress")
        }

    @Test
    fun `extreme duration values should be handled gracefully`() =
        runTest {
            // Given: Various duration scenarios
            val testDurations = listOf(1L, 50L, 10000L, 0L)

            testDurations.forEach { duration ->
                // When: Testing with different durations
                viewModel = SplashViewModel()
                viewModel.configureSplashDuration(duration)
                viewModel.resetSplash()

                // Advance reasonable time
                advanceTimeBy(1000L)
                advanceUntilIdle()

                // Then: Should maintain valid state
                val finalState = viewModel.uiState.value
                val finalProgress = viewModel.animationProgress.value

                assertTrue(
                    finalState == SplashUiState.Loading || finalState == SplashUiState.Complete,
                    "Duration $duration should result in valid state: $finalState",
                )
                assertTrue(
                    finalProgress >= 0f && finalProgress <= 1f,
                    "Duration $duration should result in valid progress: $finalProgress",
                )
            }
        }

    @Test
    fun `animation should handle rapid state queries efficiently`() =
        runTest {
            // Given: ViewModel with medium duration
            viewModel = SplashViewModel()
            viewModel.configureSplashDuration(2000L)
            viewModel.resetSplash()

            val stateReads = mutableListOf<SplashUiState>()
            val progressReads = mutableListOf<Float>()

            // When: Rapidly reading state while animation progresses
            repeat(20) { step ->
                advanceTimeBy(100L) // 100ms steps

                // Simulate rapid UI queries
                repeat(3) {
                    stateReads.add(viewModel.uiState.value)
                    progressReads.add(viewModel.animationProgress.value)
                }
            }

            advanceUntilIdle()

            // Then: Should handle rapid reads without issues
            assertTrue(stateReads.isNotEmpty(), "Should have captured state reads")
            assertTrue(progressReads.isNotEmpty(), "Should have captured progress reads")

            // All values should be valid
            stateReads.forEach { state ->
                assertTrue(
                    state == SplashUiState.Loading || state == SplashUiState.Complete,
                    "All state reads should be valid: $state",
                )
            }

            progressReads.forEach { progress ->
                assertTrue(
                    progress >= 0f && progress <= 1f,
                    "All progress reads should be valid: $progress",
                )
            }
        }

    @Test
    fun `configuration changes should not affect timing precision`() =
        runTest {
            // Given: ViewModel with initial configuration
            viewModel = SplashViewModel()
            val originalDuration = 1000L
            viewModel.configureSplashDuration(originalDuration)
            viewModel.resetSplash()

            // When: Changing configuration mid-flight (for future splash cycles)
            advanceTimeBy(500L) // Halfway through original
            val midProgress = viewModel.animationProgress.value

            viewModel.configureSplashDuration(2000L) // New duration for next cycle

            // Continue with original cycle
            advanceTimeBy(500L)
            advanceUntilIdle()

            // Then: Original cycle should complete normally
            assertEquals(SplashUiState.Complete, viewModel.uiState.value)
            assertEquals(1f, viewModel.animationProgress.value)
            assertTrue(midProgress > 0f && midProgress < 1f, "Should have been in progress at midpoint")

            // New cycle should use new duration
            viewModel.resetSplash()
            advanceTimeBy(1000L) // Half of new duration
            val newMidProgress = viewModel.animationProgress.value

            assertTrue(
                newMidProgress >= 0.4f && newMidProgress <= 0.6f,
                "New configuration should affect timing: progress=$newMidProgress at 50% of 2000ms",
            )
        }
}
