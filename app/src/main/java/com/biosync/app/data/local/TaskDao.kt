package com.biosync.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.biosync.app.data.model.MentalEffort
import com.biosync.app.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC, priority DESC")
    fun getPendingTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND mentalEffort IN (:efforts) ORDER BY dueDate ASC, priority DESC")
    fun getTasksByEffort(efforts: List<MentalEffort>): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET isCompleted = 1, completedAt = :completedAt WHERE id = :taskId")
    suspend fun completeTask(taskId: Long, completedAt: Long = System.currentTimeMillis())

    @Query("UPDATE tasks SET isCompleted = 0, completedAt = null WHERE id = :taskId")
    suspend fun uncompleteTask(taskId: Long)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1 AND mentalEffort = :effort")
    fun getCompletedCountByEffort(effort: MentalEffort): Flow<Int>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND dueDate IS NOT NULL AND dueDate <= :threshold ORDER BY dueDate ASC")
    suspend fun getTasksDueSoon(threshold: Long): List<Task>

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1 AND completedAt >= :since")
    fun getCompletedCountSince(since: Long): Flow<Int>
}
