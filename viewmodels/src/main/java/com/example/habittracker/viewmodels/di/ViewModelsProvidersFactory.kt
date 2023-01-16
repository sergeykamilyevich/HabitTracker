package com.example.habittracker.viewmodels.di

import com.example.habittracker.core_api.di.providers.CoreComponentProvider
import com.example.habittracker.db_api.di.providers.DbComponentProvider
import com.example.habittracker.network_api.di.providers.NetworkComponentProvider
import com.example.habittracker.viewmodels_api.di.providers.ViewModelsComponentProvider
import com.example.habittracker.viewmodels_impl.di.components.DaggerViewModelsComponent

object ViewModelsProvidersFactory {

    private var viewModelsComponent: ViewModelsComponentProvider? = null

    fun getViewModelsComponent(
        coreComponentProvider: CoreComponentProvider,
        dbComponentProvider: DbComponentProvider,
        networkComponentProvider: NetworkComponentProvider,

        ): ViewModelsComponentProvider =
        viewModelsComponent ?: DaggerViewModelsComponent
            .builder()
            .coreComponentProvider(coreComponentProvider)
            .dbComponentProvider(dbComponentProvider)
            .networkComponentProvider(networkComponentProvider)
            .build().also {
                viewModelsComponent = it
            }

}