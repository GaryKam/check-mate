package com.oukschub.checkmate.di

import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
}
