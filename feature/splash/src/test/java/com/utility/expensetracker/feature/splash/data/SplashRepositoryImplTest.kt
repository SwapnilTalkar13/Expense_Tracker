package com.utility.expensetracker.feature.splash.data

import com.utility.expensetracker.feature.splash.model.SplashConfig
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for SplashRepositoryImpl
 */
class SplashRepositoryImplTest {
    private lateinit var repository: SplashRepositoryImpl
    private lateinit var defaultConfig: SplashConfig

    @Before
    fun setup() {
        defaultConfig =
            SplashConfig(
                animationAsset = "test_animation.json",
                minDisplayTime = 2000L,
                fadeOutDuration = 300L,
                onSplashComplete = {},
            )
        repository = SplashRepositoryImpl(defaultConfig)
    }

    @Test
    fun `getSplashConfig returns injected configuration`() =
        runTest {
            val config = repository.getSplashConfig()

            assertEquals("test_animation.json", config.animationAsset)
            assertEquals(2000L, config.minDisplayTime)
            assertEquals(300L, config.fadeOutDuration)
        }

    @Test
    fun `trackSplashCompletion completes without error`() =
        runTest {
            // Should not throw any exceptions
            repository.trackSplashCompletion(3000L)
        }

    @Test
    fun `shouldShowSplash returns true by default`() =
        runTest {
            val shouldShow = repository.shouldShowSplash()
            assertTrue(shouldShow)
        }
}
