package com.biosync.app.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleTaskReminders(context: Context) {
        val reminderWork = PeriodicWorkRequestBuilder<TaskReminderWorker>(
            6, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TaskReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            reminderWork
        )
    }

    fun scheduleSmartSuggestions(context: Context) {
        val suggestionWork = PeriodicWorkRequestBuilder<SmartSuggestionWorker>(
            4, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SmartSuggestionWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            suggestionWork
        )
    }

    fun scheduleAll(context: Context) {
        scheduleTaskReminders(context)
        scheduleSmartSuggestions(context)
    }
}
