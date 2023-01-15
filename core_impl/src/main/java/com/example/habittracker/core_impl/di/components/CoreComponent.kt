package com.example.habittracker.core_impl.di.components

import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.providers.CoreComponentProvider
import com.example.habittracker.core_api.di.providers.CoreContextProvider
import com.example.habittracker.core_impl.di.modules.CoreModule
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [CoreContextProvider::class],
    modules = [CoreModule::class]
)
interface CoreComponent : CoreComponentProvider