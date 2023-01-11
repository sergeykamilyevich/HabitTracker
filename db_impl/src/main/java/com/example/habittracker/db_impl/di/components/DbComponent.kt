package com.example.habittracker.db_impl.di.components

import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.db_api.di.mediators.DbFacadeComponentProviders
import com.example.habittracker.db_api.di.providers.ContextProvider
import com.example.habittracker.db_impl.di.modules.DbModule
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [ContextProvider::class],
    modules = [DbModule::class]
)
interface DbComponent : DbFacadeComponentProviders