package com.example.habittracker.di.components

import android.content.Context
import com.example.habittracker.core.di.CoreProvidersFactory
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.core_api.di.providers.CoreComponentProvider
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [CoreComponentProvider::class]
)
interface CoreFacadeComponent : CoreFacadeComponentProviders {

    companion object {

        fun init(context: Context): CoreFacadeComponent {
            val coreContextProvider =
                ApplicationComponent.getApplicationComponent(context)
            val coreComponentProvider =
                CoreProvidersFactory.getCoreComponent(coreContextProvider)

            return DaggerCoreFacadeComponent
                .builder()
                .coreComponentProvider(coreComponentProvider)
                .build()
        }
    }
}