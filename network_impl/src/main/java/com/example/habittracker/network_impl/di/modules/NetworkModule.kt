package com.example.habittracker.network_impl.di.modules

import android.util.Log
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.domain.errors.IoErrorFlow
import com.example.habittracker.core_api.domain.repositories.CloudHabitRepository
import com.example.habittracker.network_api.di.providers.HabitApi
import com.example.habittracker.network_impl.data.repositories.CloudHabitRepositoryImpl
import com.example.habittracker.network_impl.data.retrofit.ApiInterceptor
import com.example.habittracker.network_impl.data.retrofit.IoErrorFlowImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface NetworkModule {

    @[Binds]
    fun bindNetworkHabitRepository(impl: CloudHabitRepositoryImpl): CloudHabitRepository

    @Binds
    fun bindIoErrorFlow(impl: IoErrorFlowImpl): IoErrorFlow

    companion object {

        @[ApplicationScope Provides]
        fun provideOkHttpClient(apiInterceptor: ApiInterceptor): OkHttpClient {
            Log.d("99999", "create OkHttpClient")
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            return OkHttpClient.Builder()
                .addInterceptor(apiInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        }

        @[ApplicationScope Provides]
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            Log.d("99999", "create Retrofit")
            val converterFactory = GsonConverterFactory.create()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .build()
        }

        @[ApplicationScope Provides]
        fun provideApiService(retrofit: Retrofit): HabitApi = retrofit.create(HabitApi::class.java)
        private const val BASE_URL = "https://droid-test-server.doubletapp.ru/"
    }
}