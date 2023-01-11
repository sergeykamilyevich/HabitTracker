package com.example.habittracker.db.di

import com.example.habittracker.db_api.di.providers.ContextProvider
import com.example.habittracker.db_api.di.providers.DbComponentProvider
import com.example.habittracker.db_impl.di.components.DaggerDbComponent

object DbProvidersFactory {

    private var dbComponent: DbComponentProvider? = null

    fun getDbComponent(contextProvider: ContextProvider): DbComponentProvider {
        return dbComponent ?: DaggerDbComponent.builder().contextProvider(contextProvider).build().also {
            dbComponent = it
        }
    }
}