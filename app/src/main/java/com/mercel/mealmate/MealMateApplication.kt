package com.mercel.mealmate

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mercel.mealmate.core.util.Constants
import com.mercel.mealmate.worker.SyncWorker
import com.mercel.mealmate.worker.WeeklyReminderWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MealMateApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        setupWorkManager()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun setupWorkManager() {
        // Weekly reminder work
        val weeklyReminderRequest = PeriodicWorkRequestBuilder<WeeklyReminderWorker>(
            7, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.WEEKLY_REMINDER_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            weeklyReminderRequest
        )

        // Sync work (daily)
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.SYNC_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}
