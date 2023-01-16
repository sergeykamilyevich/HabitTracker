package com.example.habittracker.feature_authorization.di.components

import android.app.Application
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.feature_authorization.di.modules.FeatureAuthorizationModule
import com.example.habittracker.feature_authorization.presentation.ui.AuthorizationFragment
import com.example.habittracker.viewmodels_api.di.mediators.ViewModelsFacadeComponentProviders
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        CoreFacadeComponentProviders::class,
        ViewModelsFacadeComponentProviders::class,
    ],
    modules = [
        FeatureAuthorizationModule::class
    ]
)
interface FeatureAuthorizationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
            coreFacadeComponentProviders: CoreFacadeComponentProviders,
            viewModelsFacadeComponentProviders: ViewModelsFacadeComponentProviders,
        ): FeatureAuthorizationComponent
    }

    fun inject(authorizationFragment: AuthorizationFragment)

}