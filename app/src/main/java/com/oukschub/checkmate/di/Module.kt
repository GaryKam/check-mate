package com.oukschub.checkmate.di

import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Injects dependencies throughout the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Singleton
    @Provides
    fun provideDatabase(): Database {
        return Database()
    }

    @Singleton
    @Provides
    fun provideChecklistRepository(database: Database): ChecklistRepository {
        return ChecklistRepository(database)
    }

    @Singleton
    @Provides
    fun provideUserRepository(database: Database): UserRepository {
        return UserRepository(database)
    }
}
