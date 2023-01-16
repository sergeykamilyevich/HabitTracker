package com.example.habittracker.core_impl.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.habittracker.core_api.data.store.UserPreferences
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.data.crypto.Encryption
import com.example.habittracker.core_impl.data.crypto.EncryptionImpl
import com.example.habittracker.core_impl.data.store.UserPreferencesImpl
import dagger.Module
import dagger.Provides
import java.security.KeyStore

@Module
interface CoreModule {


    companion object {

        @ApplicationScope
        @Provides
        fun provideDataStorePreferences(context: Context): DataStore<Preferences> =
            context.dataStore

        @ApplicationScope
        @Provides
        fun provideUserPreferences(
            encryption: Encryption,
            dataStore: DataStore<Preferences>,
        ): UserPreferences = UserPreferencesImpl(encryption, dataStore)

        @ApplicationScope
        @Provides
        fun provideKeyStore(): KeyStore =
            KeyStore.getInstance(ANDROID_KEY_STORE).apply {
                load(null)
            }

        @Provides
        fun provideEncryption(keyStore: KeyStore): Encryption = EncryptionImpl(keyStore)


        private const val dataStoreFile: String = "securePref"
        private val Context.dataStore by preferencesDataStore(name = dataStoreFile)
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    }
}