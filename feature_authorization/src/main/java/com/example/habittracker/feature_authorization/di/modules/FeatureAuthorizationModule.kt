package com.example.habittracker.feature_authorization.di.modules

import com.example.habittracker.viewmodels_impl.presentation.view_models.AuthorizationViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface FeatureAuthorizationModule {

    companion object {

        @Provides
        fun provideAuthorizationViewModel(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>):
                AuthorizationViewModel =
            map[AuthorizationViewModel::class.java]?.get() as? AuthorizationViewModel
                ?: throw RuntimeException("AuthorizationViewModel is absent into map")

    }
}