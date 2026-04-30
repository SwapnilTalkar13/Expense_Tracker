package com.utility.expensetracker.feature.splash.di

import com.utility.expensetracker.feature.splash.data.SplashRepositoryImpl
import com.utility.expensetracker.feature.splash.domain.SplashRepository
import com.utility.expensetracker.feature.splash.model.SplashConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for splash feature dependencies
 *
 * Provides configuration and dependencies for the splash feature,
 * making it easy to customize splash behavior across the app.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SplashModule {
    companion object {
        /**
         * Provides default splash configuration
         *
         * This can be customized by providing a different configuration
         * in the main app module or through build variants.
         */
        @Provides
        @Singleton
        fun provideSplashConfig(): SplashConfig {
            return SplashConfig(
                animationAsset = "splash_animation.json",
                minDisplayTime = 3000L,
                fadeOutDuration = 500L,
                onSplashComplete = { /* Will be overridden in UI layer */ },
            )
        }

        /**
         * Provides splash animation asset name
         *
         * Separated for easier testing and configuration.
         */
        @Provides
        @SplashAnimationAsset
        fun provideSplashAnimationAsset(): String {
            return "splash_animation.json"
        }

        /**
         * Provides splash minimum display time in milliseconds
         *
         * Can be overridden for different build variants or configurations.
         */
        @Provides
        @SplashMinDisplayTime
        fun provideSplashMinDisplayTime(): Long {
            return 3000L
        }
    }

    /**
     * Binds the repository implementation to the interface
     *
     * This allows for easy testing and future implementation swapping.
     */
    @Binds
    @Singleton
    abstract fun bindSplashRepository(splashRepositoryImpl: SplashRepositoryImpl): SplashRepository
}
