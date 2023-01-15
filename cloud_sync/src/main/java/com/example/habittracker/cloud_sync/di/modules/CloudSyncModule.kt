package com.example.habittracker.cloud_sync.di.modules

import com.example.habittracker.cloud_sync.data.repositories.SyncHabitRepositoryImpl
import com.example.habittracker.cloud_sync.domain.repositories.SyncHabitRepository
import com.example.habittracker.cloud_sync.domain.usecases.impls.*
import com.example.habittracker.cloud_sync.domain.usecases.interfaces.*
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface CloudSyncModule {

    @Binds
    fun bindSyncHabitRepository(impl: SyncHabitRepositoryImpl): SyncHabitRepository

    @Binds
    fun bindAreCloudAndDbEqualUseCase(impl: AreCloudAndDbEqualUseCaseImpl): AreCloudAndDbEqualUseCase

    @Binds
    fun bindSyncAllToCloudUseCase(impl: SyncAllToCloudUseCaseImpl): SyncAllToCloudUseCase

    companion object {

        @Provides
        fun providePutHabitAndSyncWithDbUseCase(syncHabitRepository: SyncHabitRepository): PutHabitAndSyncWithDbUseCase =
            PutHabitAndSyncWithDbUseCase(syncHabitRepository::putAndSyncWithDb)

        @Provides
        fun provideSyncAllFromCloudUseCase(syncHabitRepository: SyncHabitRepository): SyncAllFromCloudUseCase =
            SyncAllFromCloudUseCase(syncHabitRepository::syncAllFromCloud)

        @Provides
        fun provideUploadAllToCloudUseCase(syncHabitRepository: SyncHabitRepository): UploadAllToCloudUseCase =
            UploadAllToCloudUseCase(syncHabitRepository::uploadAllToCloud)
    }
}