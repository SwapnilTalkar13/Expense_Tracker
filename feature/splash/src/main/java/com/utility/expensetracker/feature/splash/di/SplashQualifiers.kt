package com.utility.expensetracker.feature.splash.di

import javax.inject.Qualifier

/**
 * Qualifier for splash animation asset name injection
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SplashAnimationAsset

/**
 * Qualifier for splash minimum display time injection
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SplashMinDisplayTime
