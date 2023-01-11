package com.example.habittracker.db_api.di.providers

import android.content.Context

interface ContextProvider {

    fun provideContext(): Context
}