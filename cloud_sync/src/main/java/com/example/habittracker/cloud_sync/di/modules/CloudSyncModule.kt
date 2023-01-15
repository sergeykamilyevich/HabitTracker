package com.example.habittracker.cloud_sync.di.modules

import com.example.habittracker.cloud_sync.data.repositories.SyncHabitRepositoryImpl
import com.example.habittracker.cloud_sync.domain.repositories.SyncHabitRepository
import com.example.habittracker.cloud_sync.domain.usecases.impls.*
import com.example.habittracker.cloud_sync.domain.usecases.interfaces.*
import dagger.Binds
import dagger.Module

@Module
interface CloudSyncModule {

    @Binds
    fun bindSyncHabitRepository(impl: SyncHabitRepositoryImpl): SyncHabitRepository

    @Binds
    fun bindSyncUseCase(impl: SyncUseCaseImpl): SyncUseCase

    @Binds
    fun bindAreCloudAndDbEqualUseCase(impl: AreCloudAndDbEqualUseCaseImpl): AreCloudAndDbEqualUseCase

    @Binds
    fun bindPutHabitAndSyncWithDbUseCase(impl: PutHabitAndSyncWithDbUseCaseImpl): PutHabitAndSyncWithDbUseCase

    @Binds
    fun bindSyncAllFromCloudUseCase(impl: SyncAllFromCloudUseCaseImpl): SyncAllFromCloudUseCase

    @Binds
    fun bindSyncAllToCloudUseCase(impl: SyncAllToCloudUseCaseImpl): SyncAllToCloudUseCase

    @Binds
    fun bindUploadAllToCloudUseCase(impl: UploadAllToCloudUseCaseImpl): UploadAllToCloudUseCase
}