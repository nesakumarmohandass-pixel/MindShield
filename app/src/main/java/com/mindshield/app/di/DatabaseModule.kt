package com.mindshield.app.di

import android.content.Context
import androidx.room.Room
import com.mindshield.app.data.local.AppDatabase
import com.mindshield.app.data.local.dao.CoachMessageDao
import com.mindshield.app.data.local.dao.UsageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mindshield.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideUsageDao(database: AppDatabase): UsageDao = database.usageDao()

    @Provides
    fun provideCoachMessageDao(database: AppDatabase): CoachMessageDao = database.coachMessageDao()
}
