package com.example.habittracker.core.di

import com.example.habittracker.core_api.di.providers.CoreComponentProvider
import com.example.habittracker.core_impl.di.components.DaggerCoreComponent

object CoreProvidersFactory {

    private var coreComponent: CoreComponentProvider? = null

    fun getCoreComponent(): CoreComponentProvider {
        return coreComponent ?: DaggerCoreComponent.builder().build().also {
            coreComponent = it
        }
    }
}