package com.example.habittracker.feature_habits.di.components

import android.app.Application
import com.example.habittracker.core_api.di.annotations.FeatureScope
import com.example.habittracker.core_api.di.mediators.CoreFacadeComponentProviders
import com.example.habittracker.db_api.di.mediators.DbFacadeComponentProviders
import com.example.habittracker.feature_habits.data.di.modules.DataModule
import com.example.habittracker.feature_habits.di.modules.FeatureHabitsModule
import com.example.habittracker.feature_habits.presentation.ui.BottomSheetFragment
import com.example.habittracker.feature_habits.presentation.ui.HabitListFragment
import com.example.habittracker.feature_habits.presentation.ui.MainActivity
import com.example.habittracker.network_api.di.mediators.NetworkFacadeComponentProviders
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(
    dependencies = [
        CoreFacadeComponentProviders::class,
        NetworkFacadeComponentProviders::class,
        DbFacadeComponentProviders::class
    ],
    modules = [
        DataModule::class,
        FeatureHabitsModule::class
    ]
)
interface FeatureHabitsComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application,
            coreFacadeComponentProviders: CoreFacadeComponentProviders,
            networkFacadeComponentProviders: NetworkFacadeComponentProviders,
            dbFacadeComponentProviders: DbFacadeComponentProviders
        ): FeatureHabitsComponent
    }

    fun inject(bottomSheetFragment: BottomSheetFragment)

    fun inject(habitListFragment: HabitListFragment)

    fun inject(mainActivity: MainActivity)

    fun habitItemFragmentComponentFactory(): HabitItemFragmentComponent.Factory

}