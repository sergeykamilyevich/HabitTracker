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

    @[Provides]
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @[Provides]
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val converterFactory = GsonConverterFactory.create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @[Provides]
    fun provideApiService(retrofit: Retrofit): HabitApi {
        return retrofit.create(HabitApi::class.java)
    }

//    companion object {
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
//    }
}