package com.example.habittracker.db.di

import com.example.habittracker.core_api.di.providers.CoreContextProvider
import com.example.habittracker.db_api.di.providers.DbComponentProvider
import com.example.habittracker.db_impl.di.components.DaggerDbComponent

object DbProvidersFactory {

    private var dbComponent: DbComponentProvider? = null

    fun getDbComponent(contextProvider: CoreContextProvider): DbComponentProvider =
        dbComponent ?: DaggerDbComponent.builder()
            .coreContextProvider(contextProvider)
            .build().also {
            dbComponent = it
        }
}