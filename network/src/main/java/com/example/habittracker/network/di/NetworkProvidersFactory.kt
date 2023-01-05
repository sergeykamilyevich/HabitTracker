package com.example.habittracker.network.di

import com.example.habittracker.network_api.di.providers.NetworkProvider
import com.example.habittracker.network_impl.di.components.DaggerNetworkComponent

object NetworkProvidersFactory {

    fun createNetworkComponent(): NetworkProvider {
        return DaggerNetworkComponent.builder().build()
    }
}