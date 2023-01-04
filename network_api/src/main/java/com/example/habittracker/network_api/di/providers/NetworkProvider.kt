package com.example.habittracker.network_api.di.providers

interface NetworkProvider {

    fun provideApiService(): HabitApi2
}