package com.example.habittracker.feature_habits.di.components

import android.app.Application
import com.example.habittracker.cloud_sync.di.modules.CloudSyncModule
import com.example.habittracker.core_api.di.annotations.FeatureScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.db_api.di.mediators.DbFacadeComponentProviders
import com.example.habittracker.feature_habits.di.modules.FeatureHabitsModule
import com.example.habittracker.feature_habits.presentation.ui.HabitListFragment
import com.example.habittracker.feature_habits.presentation.ui.MainActivity
import com.example.habittracker.network_api.di.mediators.NetworkFacadeComponentProviders
import com.example.habittracker.viewmodels_api.di.mediators.ViewModelsFacadeComponentProviders
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(
    dependencies = [
        CoreFacadeComponentProviders::class,
        NetworkFacadeComponentProviders::class,
        DbFacadeComponentProviders::class,
        ViewModelsFacadeComponentProviders::class,
    ],
    modules = [
        FeatureHabitsModule::class,
        CloudSyncModule::class
    ]
)
interface FeatureHabitsComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
            coreFacadeComponentProviders: CoreFacadeComponentProviders,
            networkFacadeComponentProviders: NetworkFacadeComponentProviders,
            dbFacadeComponentProviders: DbFacadeComponentProviders,
            viewModelsFacadeComponentProviders: ViewModelsFacadeComponentProviders,
        ): FeatureHabitsComponent
    }

    fun inject(habitListFragment: HabitListFragment)

    fun inject(mainActivity: MainActivity)

    fun habitItemFragmentComponentFactory(): HabitItemFragmentComponent.Factory

}