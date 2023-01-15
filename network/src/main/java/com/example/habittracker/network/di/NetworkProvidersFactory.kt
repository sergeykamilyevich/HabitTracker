package com.example.habittracker.network.di

import com.example.habittracker.core_api.di.providers.CoreComponentProvider
import com.example.habittracker.core_api.di.providers.CoreContextProvider
import com.example.habittracker.network_api.di.providers.NetworkComponentProvider
import com.example.habittracker.network_impl.di.components.DaggerNetworkComponent

object NetworkProvidersFactory {

    private var networkComponent: NetworkComponentProvider? = null

    fun getNetworkComponent(coreComponentProvider: CoreComponentProvider): NetworkComponentProvider =
        networkComponent ?: DaggerNetworkComponent
            .builder()
            .coreComponentProvider(coreComponentProvider)
            .build().also {
                networkComponent = it
            }

}