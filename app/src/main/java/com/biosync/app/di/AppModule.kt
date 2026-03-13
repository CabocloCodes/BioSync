package com.biosync.app.di

import android.content.Context
import androidx.room.Room
import com.biosync.app.data.local.BioSyncDatabase
import com.biosync.app.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BioSyncDatabase {
        return Room.databaseBuilder(
            context,
            BioSyncDatabase::class.java,
            "biosync_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: BioSyncDatabase): TaskDao {
        return database.taskDao()
    }
}
