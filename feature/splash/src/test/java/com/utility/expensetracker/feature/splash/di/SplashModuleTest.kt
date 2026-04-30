package com.utility.expensetracker.feature.splash.di

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests for SplashModule configuration
 *
 * Simple unit tests that verify the module provides correct configuration
 * without requiring Hilt DI container setup.
 */
class SplashModuleTest {
    @Test
    fun `provideSplashConfig returns correct configuration`() {
        val config = SplashModule.provideSplashConfig()

        assertNotNull(config)
        assertEquals("splash_animation.json", config.animationAsset)
        assertEquals(3000L, config.minDisplayTime)
        assertEquals(500L, config.fadeOutDuration)
    }

    @Test
    fun `provideSplashAnimationAsset returns correct asset name`() {
        val asset = SplashModule.provideSplashAnimationAsset()
        assertEquals("splash_animation.json", asset)
    }

    @Test
    fun `provideSplashMinDisplayTime returns correct duration`() {
        val duration = SplashModule.provideSplashMinDisplayTime()
        assertEquals(3000L, duration)
    }
}
