package com.example.habittracker.di.components

import android.content.Context
import com.example.habittracker.core.di.CoreProvidersFactory
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.network.di.NetworkProvidersFactory
import com.example.habittracker.network_api.di.mediators.NetworkFacadeComponentProviders
import com.example.habittracker.network_api.di.providers.NetworkComponentProvider
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [NetworkComponentProvider::class]
)
interface NetworkFacadeComponent : NetworkFacadeComponentProviders {

    companion object {

        fun init(context: Context): NetworkFacadeComponent =
            DaggerNetworkFacadeComponent
                .builder()
                .networkComponentProvider(
                    NetworkProvidersFactory
                        .getNetworkComponent(
                            CoreProvidersFactory
                                .getCoreComponent(
                                    ApplicationComponent
                                        .getApplicationComponent(context)
                                )
                        )
                )
                .build()
    }
}