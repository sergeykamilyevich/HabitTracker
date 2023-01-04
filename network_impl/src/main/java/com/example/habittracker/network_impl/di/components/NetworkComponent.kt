package com.example.habittracker.network_impl.di.components

import com.example.habittracker.network_api.di.providers.HabitApi2
import com.example.habittracker.network_api.di.providers.NetworkProvider
import com.example.habittracker.network_impl.di.modules.NetworkModule
import dagger.Component

@Component(
    modules = [NetworkModule::class]
)
interface NetworkComponent : NetworkProvider {

    override fun provideApiService(): HabitApi2
}