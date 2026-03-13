package com.biosync.app.data.repository

import com.biosync.app.data.local.TaskDao
import com.biosync.app.data.model.EnergyLevel
import com.biosync.app.data.model.MentalEffort
import com.biosync.app.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getPendingTasks(): Flow<List<Task>> = taskDao.getPendingTasks()

    fun getCompletedTasks(): Flow<List<Task>> = taskDao.getCompletedTasks()

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTasksForEnergyLevel(energyLevel: EnergyLevel): Flow<List<Task>> {
        return taskDao.getTasksByEffort(energyLevel.suggestedEfforts())
    }

    suspend fun getTaskById(id: Long): Task? = taskDao.getTaskById(id)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun completeTask(taskId: Long) = taskDao.completeTask(taskId)

    suspend fun uncompleteTask(taskId: Long) = taskDao.uncompleteTask(taskId)

    fun getCompletedCountByEffort(effort: MentalEffort): Flow<Int> =
        taskDao.getCompletedCountByEffort(effort)

    suspend fun getTasksDueSoon(threshold: Long): List<Task> =
        taskDao.getTasksDueSoon(threshold)

    fun getCompletedCountSince(since: Long): Flow<Int> =
        taskDao.getCompletedCountSince(since)
}
