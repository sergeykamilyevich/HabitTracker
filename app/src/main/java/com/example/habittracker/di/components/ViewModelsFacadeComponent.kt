package com.example.habittracker.di.components

import android.content.Context
import com.example.habittracker.core.di.CoreProvidersFactory
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.providers.CoreContextProvider
import com.example.habittracker.db.di.DbProvidersFactory
import com.example.habittracker.network.di.NetworkProvidersFactory
import com.example.habittracker.viewmodels.di.ViewModelsProvidersFactory
import com.example.habittracker.viewmodels_api.di.mediators.ViewModelsFacadeComponentProviders
import com.example.habittracker.viewmodels_api.di.providers.ViewModelsComponentProvider
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        ViewModelsComponentProvider::class,
        CoreContextProvider::class,
    ]
)
interface ViewModelsFacadeComponent : ViewModelsFacadeComponentProviders {

    companion object {

        fun init(context: Context): ViewModelsFacadeComponent {
            val coreContextProvider =
                ApplicationComponent.getApplicationComponent(context)
            val coreComponentProvider =
                CoreProvidersFactory.getCoreComponent(coreContextProvider)
            val networkComponentProvider =
                NetworkProvidersFactory.getNetworkComponent(coreComponentProvider)
            val dbComponentProvider =
                DbProvidersFactory.getDbComponent(coreContextProvider)
            return DaggerViewModelsFacadeComponent
                .builder()
                .viewModelsComponentProvider(
                    ViewModelsProvidersFactory
                        .getViewModelsComponent(
                            coreComponentProvider = coreComponentProvider,
                            dbComponentProvider = dbComponentProvider,
                            networkComponentProvider = networkComponentProvider
                        )
                )
                .coreContextProvider(coreContextProvider)
                .build()
        }

    }
}