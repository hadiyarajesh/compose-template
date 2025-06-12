package com.hadiyarajesh.composetemplate.di

import android.content.Context
import androidx.room.Room
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.database.AppDatabase
import com.hadiyarajesh.composetemplate.data.database.DatabaseInitializer
import com.hadiyarajesh.composetemplate.data.database.dao.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Dagger-Hilt module that provides dependencies related to the Room database.
 *
 * This module includes:
 * - A singleton instance of [AppDatabase]
 * - A singleton instance of [ImageDao], retrieved from the database.
 *
 * Annotated with [@InstallIn(SingletonComponent::class)] to ensure the
 * provided instances live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        imageDaoProvider: Provider<ImageDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, context.getString(
                R.string.app_name
            )
        ).addCallback(
            /**
             * Attach [DatabaseInitializer] as callback to the database
             */
            DatabaseInitializer(context = context, imageDaoProvider = imageDaoProvider)
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideMessageDao(appDatabase: AppDatabase): ImageDao = appDatabase.imageDao()
}
