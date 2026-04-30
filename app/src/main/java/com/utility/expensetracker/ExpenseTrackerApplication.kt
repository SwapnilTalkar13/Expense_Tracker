package com.utility.expensetracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for ExpenseTracker
 *
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class ExpenseTrackerApplication : Application()
