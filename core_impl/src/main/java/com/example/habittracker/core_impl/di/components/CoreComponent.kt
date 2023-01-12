package com.example.habittracker.core_impl.di.components

import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.core_impl.di.modules.CoreModule
import dagger.Component

@ApplicationScope
@Component(
    modules = [CoreModule::class]
)
interface CoreComponent : CoreFacadeComponentProviders