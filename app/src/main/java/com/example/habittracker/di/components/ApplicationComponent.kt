package com.example.habittracker.di.components

import android.content.Context
import com.example.habittracker.db_api.di.providers.ContextProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface ApplicationComponent : ContextProvider {

    companion object {

        private var applicationComponent: ApplicationComponent? = null

        fun getApplicationComponent(context: Context): ContextProvider =
            applicationComponent ?: DaggerApplicationComponent
                .factory()
                .create(context)
                .also {
                    applicationComponent = it
                }
    }

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }

}

