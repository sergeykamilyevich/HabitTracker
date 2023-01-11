package com.example.habittracker.network.di

import com.example.habittracker.network_api.di.providers.NetworkComponentProvider
import com.example.habittracker.network_impl.di.components.DaggerNetworkComponent

object NetworkProvidersFactory {

    val networkComponent: NetworkComponentProvider = DaggerNetworkComponent.builder().build()

}