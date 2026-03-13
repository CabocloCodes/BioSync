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
import com.biosync.app.data.model.MentalEffort
import com.biosync.app.data.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.Calendar

@HiltWorker
class SmartSuggestionWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskRepository: TaskRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val suggestedEffort = when {
            hour in 6..10 -> MentalEffort.HIGH    // Morning: peak energy
            hour in 11..14 -> MentalEffort.MEDIUM  // Midday: moderate energy
            hour in 15..18 -> MentalEffort.MEDIUM  // Afternoon: moderate energy
            else -> MentalEffort.LOW               // Evening/night: low energy
        }

        val pendingTasks = taskRepository.getPendingTasks().first()
        val matchingTasks = pendingTasks.filter { it.mentalEffort == suggestedEffort }

        if (matchingTasks.isNotEmpty()) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                1,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val message = when (suggestedEffort) {
                MentalEffort.HIGH -> "Sua energia está no pico! Que tal enfrentar uma tarefa desafiadora?"
                MentalEffort.MEDIUM -> "Bom momento para tarefas moderadas. Você tem ${matchingTasks.size} pendentes."
                MentalEffort.LOW -> "Hora de tarefas leves. Que tal \"${matchingTasks.first().title}\"?"
            }

            val notification = NotificationCompat.Builder(context, BioSyncApp.CHANNEL_SMART_SUGGESTION)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("BioSync - Sugestão Inteligente")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID_SUGGESTION, notification)
        }

        return Result.success()
    }

    companion object {
        const val NOTIFICATION_ID_SUGGESTION = 1002
        const val WORK_NAME = "smart_suggestion_work"
    }
}
