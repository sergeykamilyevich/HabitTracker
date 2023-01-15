package com.example.habittracker.di.components

import android.content.Context
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.db.di.DbProvidersFactory
import com.example.habittracker.db_api.di.mediators.DbFacadeComponentProviders
import com.example.habittracker.db_api.di.providers.DbComponentProvider
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [DbComponentProvider::class]
)
interface DbFacadeComponent : DbFacadeComponentProviders {

    companion object {

        fun init(context: Context): DbFacadeComponent =
            DaggerDbFacadeComponent
                .builder()
                .dbComponentProvider(DbProvidersFactory.getDbComponent(ApplicationComponent.getApplicationComponent(context)))
                .build()
    }
}