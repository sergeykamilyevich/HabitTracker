package com.example.habittracker.di.modules

import com.example.habittracker.data.network.HabitApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [DataAbstractModule::class])
object DataModule {

    @[Provides Singleton]
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", API_TOKEN)
                    .build()
                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(interceptor)
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
    private const val API_TOKEN = "05b550ee-1713-43f1-a842-9815d354460d"
}