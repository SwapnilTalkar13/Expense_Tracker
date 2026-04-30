package com.utility.expensetracker.feature.splash.presentation

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for SplashUiState sealed class
 *
 * Tests state object equality, error state construction, and type safety.
 */
class SplashUiStateTest {
    @Test
    fun `loading state should be singleton object`() {
        // Given: Two loading state instances
        val loading1 = SplashUiState.Loading
        val loading2 = SplashUiState.Loading

        // When: Comparing instances
        // Then: They should be the same object
        assertEquals(loading1, loading2)
        assertEquals(loading1.hashCode(), loading2.hashCode())
    }

    @Test
    fun `complete state should be singleton object`() {
        // Given: Two complete state instances
        val complete1 = SplashUiState.Complete
        val complete2 = SplashUiState.Complete

        // When: Comparing instances
        // Then: They should be the same object
        assertEquals(complete1, complete2)
        assertEquals(complete1.hashCode(), complete2.hashCode())
    }

    @Test
    fun `loading and complete states should be different`() {
        // Given: Loading and complete states
        val loading = SplashUiState.Loading
        val complete = SplashUiState.Complete

        // When: Comparing different states
        // Then: They should not be equal
        assertNotEquals(loading as Any, complete as Any)
    }

    @Test
    fun `error state should contain error and optional details`() {
        // Given: Error with details
        val errorWithDetails =
            SplashUiState.Error(
                error = SplashError.LoadingFailed,
                details = "Network timeout occurred",
            )

        // When: Accessing properties
        // Then: Properties should be correctly set
        assertEquals(SplashError.LoadingFailed, errorWithDetails.error)
        assertEquals("Network timeout occurred", errorWithDetails.details)
    }

    @Test
    fun `error state without details should have null details`() {
        // Given: Error without details
        val errorWithoutDetails =
            SplashUiState.Error(
                error = SplashError.Generic,
            )

        // When: Accessing details
        // Then: Details should be null
        assertEquals(SplashError.Generic, errorWithoutDetails.error)
        assertNull(errorWithoutDetails.details)
    }

    @Test
    fun `error states with same error and details should be equal`() {
        // Given: Two error states with same properties
        val error1 =
            SplashUiState.Error(
                error = SplashError.NetworkConnection,
                details = "Connection failed",
            )
        val error2 =
            SplashUiState.Error(
                error = SplashError.NetworkConnection,
                details = "Connection failed",
            )

        // When: Comparing error states
        // Then: They should be equal
        assertEquals(error1, error2)
        assertEquals(error1.hashCode(), error2.hashCode())
    }

    @Test
    fun `error states with different errors should not be equal`() {
        // Given: Error states with different error types
        val error1 =
            SplashUiState.Error(
                error = SplashError.LoadingFailed,
                details = "Failed to load",
            )
        val error2 =
            SplashUiState.Error(
                error = SplashError.NetworkConnection,
                details = "Failed to load",
            )

        // When: Comparing error states
        // Then: They should not be equal
        assertNotEquals(error1, error2)
    }

    @Test
    fun `error states with different details should not be equal`() {
        // Given: Error states with different details
        val error1 =
            SplashUiState.Error(
                error = SplashError.Generic,
                details = "First error message",
            )
        val error2 =
            SplashUiState.Error(
                error = SplashError.Generic,
                details = "Second error message",
            )

        // When: Comparing error states
        // Then: They should not be equal
        assertNotEquals(error1, error2)
    }

    @Test
    fun `error state should not equal loading or complete states`() {
        // Given: Error state and other states
        val error = SplashUiState.Error(SplashError.LoadingFailed)
        val loading = SplashUiState.Loading
        val complete = SplashUiState.Complete

        // When: Comparing with other states
        // Then: Error should not equal other states
        assertNotEquals(error as Any, loading as Any)
        assertNotEquals(error as Any, complete as Any)
        assertNotEquals(loading as Any, error as Any)
        assertNotEquals(complete as Any, error as Any)
    }

    @Test
    fun `when clause should handle all state types`() {
        // Given: List of all possible states
        val states =
            listOf(
                SplashUiState.Loading,
                SplashUiState.Complete,
                SplashUiState.Error(SplashError.LoadingFailed),
                SplashUiState.Error(SplashError.NetworkConnection, "Details"),
                SplashUiState.Error(SplashError.AnimationLoad),
                SplashUiState.Error(SplashError.NetworkTimeout),
                SplashUiState.Error(SplashError.Generic),
            )

        // When: Processing each state in when clause
        states.forEach { state ->
            val result =
                when (state) {
                    is SplashUiState.Loading -> "loading"
                    is SplashUiState.Complete -> "complete"
                    is SplashUiState.Error -> "error_${state.error::class.simpleName}"
                }

            // Then: Each state should be handled correctly
            when (state) {
                is SplashUiState.Loading -> assertEquals("loading", result)
                is SplashUiState.Complete -> assertEquals("complete", result)
                is SplashUiState.Error -> assertTrue(result.startsWith("error_"), "Error state should be handled correctly")
            }
        }
    }
}
