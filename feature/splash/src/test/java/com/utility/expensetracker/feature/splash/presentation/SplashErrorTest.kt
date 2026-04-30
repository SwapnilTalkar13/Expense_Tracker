package com.utility.expensetracker.feature.splash.presentation

import com.utility.expensetracker.feature.splash.R
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Unit tests for SplashError sealed class
 *
 * Tests error types, string resources, and equality comparisons.
 */
class SplashErrorTest {
    @Test
    fun `LoadingFailed should have correct message resource`() {
        // Given: LoadingFailed error
        val error = SplashError.LoadingFailed

        // When: Accessing message resource
        // Then: Should have correct resource ID
        assertEquals(R.string.splash_error_failed_to_load, error.messageRes)
    }

    @Test
    fun `NetworkConnection should have correct message resource`() {
        // Given: NetworkConnection error
        val error = SplashError.NetworkConnection

        // When: Accessing message resource
        // Then: Should have correct resource ID
        assertEquals(R.string.splash_error_network_connection, error.messageRes)
    }

    @Test
    fun `AnimationLoad should have correct message resource`() {
        // Given: AnimationLoad error
        val error = SplashError.AnimationLoad

        // When: Accessing message resource
        // Then: Should have correct resource ID
        assertEquals(R.string.splash_error_animation_load, error.messageRes)
    }

    @Test
    fun `NetworkTimeout should have correct message resource`() {
        // Given: NetworkTimeout error
        val error = SplashError.NetworkTimeout

        // When: Accessing message resource
        // Then: Should have correct resource ID
        assertEquals(R.string.splash_error_network_timeout, error.messageRes)
    }

    @Test
    fun `Generic should have correct message resource`() {
        // Given: Generic error
        val error = SplashError.Generic

        // When: Accessing message resource
        // Then: Should have correct resource ID
        assertEquals(R.string.splash_error_generic, error.messageRes)
    }

    @Test
    fun `same error types should be equal`() {
        // Given: Same error type instances
        val error1 = SplashError.LoadingFailed
        val error2 = SplashError.LoadingFailed

        // When: Comparing instances
        // Then: They should be equal
        assertEquals(error1, error2)
        assertEquals(error1.hashCode(), error2.hashCode())
    }

    @Test
    fun `different error types should not be equal`() {
        // Given: Different error types
        val loadingError = SplashError.LoadingFailed
        val networkError = SplashError.NetworkConnection
        val animationError = SplashError.AnimationLoad
        val timeoutError = SplashError.NetworkTimeout
        val genericError = SplashError.Generic

        val allErrors = listOf(loadingError, networkError, animationError, timeoutError, genericError)

        // When: Comparing different error types
        for (i in allErrors.indices) {
            for (j in allErrors.indices) {
                if (i != j) {
                    // Then: Different errors should not be equal
                    assertNotEquals(
                        allErrors[i] as Any,
                        allErrors[j] as Any,
                        "Error ${allErrors[i]::class.simpleName} should not equal ${allErrors[j]::class.simpleName}",
                    )
                }
            }
        }
    }

    @Test
    fun `all error types should have unique message resources`() {
        // Given: All error types
        val errors =
            listOf(
                SplashError.LoadingFailed,
                SplashError.NetworkConnection,
                SplashError.AnimationLoad,
                SplashError.NetworkTimeout,
                SplashError.Generic,
            )

        // When: Collecting message resources
        val messageResources = errors.map { it.messageRes }

        // Then: All message resources should be unique
        assertEquals(
            messageResources.toSet().size,
            messageResources.size,
            "All error types should have unique message resources",
        )
    }

    @Test
    fun `all error types should be data objects`() {
        // Given: Error type instances
        val error1 = SplashError.LoadingFailed
        val error2 = SplashError.LoadingFailed

        // When: Comparing references
        // Then: They should be the same object (data objects are singletons)
        assertEquals(error1, error2)
        assertEquals(error1.toString(), error2.toString())
    }

    @Test
    fun `error types should have meaningful string representations`() {
        // Given: All error types
        val errors =
            mapOf(
                SplashError.LoadingFailed to "LoadingFailed",
                SplashError.NetworkConnection to "NetworkConnection",
                SplashError.AnimationLoad to "AnimationLoad",
                SplashError.NetworkTimeout to "NetworkTimeout",
                SplashError.Generic to "Generic",
            )

        // When: Converting to string
        errors.forEach { (error, expectedName) ->
            val stringRepresentation = error.toString()

            // Then: String representation should contain the error type name
            assertTrue(
                stringRepresentation.contains(expectedName),
                "Error $expectedName should have correct string representation: $stringRepresentation",
            )
        }
    }

    @Test
    fun `when clause should handle all error types exhaustively`() {
        // Given: All error types
        val errors =
            listOf(
                SplashError.LoadingFailed,
                SplashError.NetworkConnection,
                SplashError.AnimationLoad,
                SplashError.NetworkTimeout,
                SplashError.Generic,
            )

        // When: Processing each error in when clause
        errors.forEach { error ->
            val handled =
                when (error) {
                    SplashError.LoadingFailed -> true
                    SplashError.NetworkConnection -> true
                    SplashError.AnimationLoad -> true
                    SplashError.NetworkTimeout -> true
                    SplashError.Generic -> true
                }

            // Then: Each error should be handled
            assertEquals(true, handled, "Error $error should be handled in when clause")
        }
    }

    @Test
    fun `error message resources should be valid resource IDs`() {
        // Given: All error types
        val errors =
            listOf(
                SplashError.LoadingFailed,
                SplashError.NetworkConnection,
                SplashError.AnimationLoad,
                SplashError.NetworkTimeout,
                SplashError.Generic,
            )

        // When: Checking message resource IDs
        errors.forEach { error ->
            // Then: Message resource ID should be positive (valid resource ID)
            assertEquals(
                true,
                error.messageRes > 0,
                "Error ${error::class.simpleName} should have valid positive resource ID, but got ${error.messageRes}",
            )
        }
    }
}
