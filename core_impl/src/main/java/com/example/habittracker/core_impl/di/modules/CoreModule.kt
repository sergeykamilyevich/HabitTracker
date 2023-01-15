package com.example.habittracker.core_impl.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface CoreModule {


    companion object {

        @Singleton
        @Provides
        fun provideDataStorePreferences(context: Context): DataStore<Preferences> =
            context.dataStore


//        @Singleton
//        @Provides
//        fun provideUserPreferences(
//            encryption: Encryption,
//            dataStore: DataStore<Preferences>,
//        ): UserPreferences {
//            return UserPreferences(encryption, dataStore)
//        }

        private const val dataStoreFile: String = "securePref"
        private val Context.dataStore by preferencesDataStore(name = dataStoreFile)
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    }
}