package com.example.habittracker.data.di.modules

import com.example.habittracker.data.network.retrofit.ApiInterceptor
import com.example.habittracker.data.network.retrofit.HabitApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object CloudModule {

    @[Provides Singleton]
    fun provideOkHttpClient(apiInterceptor: ApiInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @[Provides Singleton]
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val converterFactory = GsonConverterFactory.create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @[Provides Singleton]
    fun provideApiService(retrofit: Retrofit): HabitApi = retrofit.create(HabitApi::class.java)

    private const val BASE_URL = "https://droid-test-server.doubletapp.ru/"

}