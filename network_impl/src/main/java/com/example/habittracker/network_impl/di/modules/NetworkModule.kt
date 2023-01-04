package com.example.habittracker.network_impl.di.modules

import com.example.habittracker.domain.errors.IoErrorFlow
import com.example.habittracker.network_api.di.providers.HabitApi2
import com.example.habittracker.network_impl.retrofit.ApiInterceptor2
import com.example.habittracker.network_impl.retrofit.IoErrorFlowImpl2
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {

    @[Provides]
    fun provideIoErrorFlow(impl: IoErrorFlowImpl2): IoErrorFlow { //TODO binds
        return impl
    }

    @[Provides]
    fun provideOkHttpClient(apiInterceptor: ApiInterceptor2): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .addInterceptor(loggingInterceptor)
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
    fun provideApiService(retrofit: Retrofit): HabitApi2 = retrofit.create(HabitApi2::class.java)
    private const val BASE_URL = "https://droid-test-server.doubletapp.ru/"
}