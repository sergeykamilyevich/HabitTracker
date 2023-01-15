package com.example.habittracker.db_impl.di.components

import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.providers.CoreContextProvider
import com.example.habittracker.db_api.di.mediators.DbFacadeComponentProviders
import com.example.habittracker.db_impl.di.modules.DbModule
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [CoreContextProvider::class],
    modules = [DbModule::class]
)
interface DbComponent : DbFacadeComponentProviders