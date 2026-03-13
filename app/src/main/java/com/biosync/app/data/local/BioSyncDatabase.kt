package com.biosync.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.biosync.app.data.model.Task
import com.biosync.app.util.Converters

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BioSyncDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
