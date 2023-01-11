package com.example.habittracker.di.components

import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.network.di.NetworkProvidersFactory
import com.example.habittracker.network_api.di.mediators.NetworkFacadeComponentProviders
import com.example.habittracker.network_api.di.providers.NetworkComponentProvider
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [NetworkComponentProvider::class]
)
interface NetworkFacadeComponent : NetworkFacadeComponentProviders {

    companion object {

        fun init(): NetworkFacadeComponent =
            DaggerNetworkFacadeComponent
                .builder()
                .networkComponentProvider(NetworkProvidersFactory.networkComponent)
                .build()
    }
}