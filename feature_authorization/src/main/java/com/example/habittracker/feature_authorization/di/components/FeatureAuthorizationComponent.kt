package com.example.habittracker.feature_authorization.di.components

import android.app.Application
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.feature_authorization.presentation.ui.AuthorizationFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        CoreFacadeComponentProviders::class
    ],
    modules = [
//        FeatureHabitFilterModule::class
    ]
)
interface FeatureAuthorizationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
            coreFacadeComponentProviders: CoreFacadeComponentProviders
        ): FeatureAuthorizationComponent
    }

    fun inject(authorizationFragment: AuthorizationFragment)

}