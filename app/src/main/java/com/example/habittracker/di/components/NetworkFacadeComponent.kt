package com.example.habittracker.di.components

import com.example.habittracker.network.di.NetworkProvidersFactory
import com.example.habittracker.network_api.di.mediators.NetworkFacadeProviders
import com.example.habittracker.network_api.di.providers.NetworkProvider
import dagger.Component

@Component(
    dependencies = [NetworkProvider::class]
)
interface NetworkFacadeComponent : NetworkFacadeProviders {

    companion object {

        fun init(): NetworkFacadeComponent =
            DaggerNetworkFacadeComponent
                .builder()
                .networkProvider(NetworkProvidersFactory.createNetworkComponent())
                .build()
    }
}