package com.biosync.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BioSyncApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val taskReminderChannel = NotificationChannel(
            CHANNEL_TASK_REMINDER,
            "Task Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Reminders for upcoming task deadlines"
        }

        val smartSuggestionChannel = NotificationChannel(
            CHANNEL_SMART_SUGGESTION,
            "Smart Suggestions",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Intelligent task suggestions based on your energy patterns"
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(taskReminderChannel)
        notificationManager.createNotificationChannel(smartSuggestionChannel)
    }

    companion object {
        const val CHANNEL_TASK_REMINDER = "task_reminder"
        const val CHANNEL_SMART_SUGGESTION = "smart_suggestion"
    }
}
