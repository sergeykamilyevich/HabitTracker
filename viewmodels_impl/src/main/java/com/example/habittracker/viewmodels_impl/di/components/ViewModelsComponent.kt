package com.example.habittracker.viewmodels_impl.di.components

import com.example.habittracker.cloud_sync.di.modules.CloudSyncModule
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.providers.CoreComponentProvider
import com.example.habittracker.db_api.di.providers.DbComponentProvider
import com.example.habittracker.network_api.di.providers.NetworkComponentProvider
import com.example.habittracker.viewmodels_api.di.providers.ViewModelsComponentProvider
import com.example.habittracker.viewmodels_impl.di.modules.ViewModelsModule
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [CoreComponentProvider::class,
        DbComponentProvider::class,
        NetworkComponentProvider::class
    ],
    modules = [
        ViewModelsModule::class,
        CloudSyncModule::class
    ]
)
interface ViewModelsComponent :
    ViewModelsComponentProvider,
    DbComponentProvider,
    NetworkComponentProvider