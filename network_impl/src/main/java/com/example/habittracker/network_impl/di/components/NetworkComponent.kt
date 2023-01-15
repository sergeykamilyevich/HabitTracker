package com.example.habittracker.network_impl.di.components

import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.providers.CoreComponentProvider
import com.example.habittracker.network_api.di.providers.NetworkComponentProvider
import com.example.habittracker.network_impl.di.modules.NetworkModule
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [CoreComponentProvider::class],
    modules = [NetworkModule::class]
)
interface NetworkComponent : NetworkComponentProvider