package com.biosync.app.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.biosync.app.BioSyncApp
import com.biosync.app.MainActivity
import com.biosync.app.R
import com.biosync.app.data.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TaskReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskRepository: TaskRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val twentyFourHours = 24 * 60 * 60 * 1000L
        val threshold = System.currentTimeMillis() + twentyFourHours

        val tasksDueSoon = taskRepository.getTasksDueSoon(threshold)

        if (tasksDueSoon.isNotEmpty()) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val taskCount = tasksDueSoon.size
            val firstTask = tasksDueSoon.first()

            val notification = NotificationCompat.Builder(context, BioSyncApp.CHANNEL_TASK_REMINDER)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Tarefas com prazo próximo!")
                .setContentText(
                    if (taskCount == 1) "\"${firstTask.title}\" vence em breve"
                    else "$taskCount tarefas vencem em breve"
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID_REMINDER, notification)
        }

        return Result.success()
    }

    companion object {
        const val NOTIFICATION_ID_REMINDER = 1001
        const val WORK_NAME = "task_reminder_work"
    }
}
