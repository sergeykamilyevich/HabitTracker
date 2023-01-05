package com.example.habittracker.feature_habits.di.components

import android.app.Application
import com.example.habittracker.data.di.modules.CloudModule
import com.example.habittracker.data.di.modules.DataModule
import com.example.habittracker.feature_habits.di.modules.FeatureHabitsModule
import com.example.habittracker.feature_habits.presentation.ui.BottomSheetFragment
import com.example.habittracker.feature_habits.presentation.ui.HabitListFragment
import com.example.habittracker.feature_habits.presentation.ui.MainActivity
import com.example.habittracker.network_api.di.mediators.NetworkFacadeProviders
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkFacadeProviders::class],
    modules = [
        CloudModule::class,
        DataModule::class,
        FeatureHabitsModule::class
    ]
)
interface FeatureHabitsComponent {

    @Component.Builder
    interface Builder {

        fun application(@BindsInstance application: Application): Builder
        fun networkFacadeProviders(networkFacadeProviders: NetworkFacadeProviders): Builder

        fun build(): FeatureHabitsComponent

    }

    fun inject(bottomSheetFragment: BottomSheetFragment)

    fun inject(habitListFragment: HabitListFragment)

    fun inject(mainActivity: MainActivity)

    fun habitItemFragmentComponentFactory(): HabitItemFragmentComponent.Factory

}