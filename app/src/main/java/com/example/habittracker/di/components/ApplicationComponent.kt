package com.example.habittracker.di.components

import android.content.Context
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.providers.CoreContextProvider
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component
interface ApplicationComponent : CoreContextProvider {

    companion object {

        private var applicationComponent: ApplicationComponent? = null

        fun getApplicationComponent(context: Context): CoreContextProvider =
            applicationComponent ?: DaggerApplicationComponent
                .factory()
                .create(context)
                .also {
                    applicationComponent = it
                }
    }

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): ApplicationComponent
    }

}

